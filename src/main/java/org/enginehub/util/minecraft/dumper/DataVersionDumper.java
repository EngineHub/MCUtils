package org.enginehub.util.minecraft.dumper;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.state.IProperty;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import org.enginehub.util.minecraft.util.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataVersionDumper {

    public static void main(String[] args) {
        setupGame();
        (new DataVersionDumper(new File("output/" + 1631 + ".json"))).run();
    }

    private File file;
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public DataVersionDumper(File file) {
        this.file = file;
    }

    @SuppressWarnings("unchecked")
    private <T> Map<String, List<String>> getTags(TagsProvider<T> provider, IRegistry<T> registry) {
        Map<String, List<String>> tagCollector = new HashMap<>();

        Map<Object, Object> tags = (Map<Object, Object>) ReflectionUtil.getField(provider, TagsProvider.class, "field_200434_b");

        Map<String, List<Tag.TagEntry<T>>> deferred = new HashMap<>();
        tags.forEach((key, value) -> {
            Tag<T> keyTag = (Tag<T>) key;
            ((Set<Tag.ITagEntry<T>>) ReflectionUtil.getField(value, Tag.Builder.class, "field_200052_a")).forEach(entry -> {
                if (entry instanceof Tag.TagEntry) {
                    deferred.computeIfAbsent(keyTag.func_199886_b().toString(), kk -> new ArrayList<>()).add((Tag.TagEntry<T>) entry);
                } else {
                    tagCollector.computeIfAbsent(keyTag.func_199886_b().toString(), kk -> new ArrayList<>()).addAll(
                            ((Collection<T>) ReflectionUtil.getField(entry, Tag.ListEntry.class, "field_200165_a")).stream()
                                    .map(block -> registry.func_177774_c(block).toString())
                                    .collect(Collectors.toList()));
                }
            });
        });

        while (!deferred.isEmpty()) {
            Map<String, List<Tag.TagEntry<T>>> iter = new HashMap<>(deferred);
            deferred.clear();
            iter.forEach((key, value) -> {
                for (Tag.TagEntry<T> entry : value) {
                    if (deferred.containsKey(entry.func_200577_a().toString())) {
                        deferred.put(key, value);
                    } else if (!tagCollector.containsKey(entry.func_200577_a().toString())) {
                        throw new RuntimeException("Missing tag! " + entry.func_200577_a().toString());
                    } else {
                        tagCollector.computeIfAbsent(key, kk -> new ArrayList<>()).addAll(tagCollector.get(entry.func_200577_a().toString()));
                    }
                }
            });
        }

        return tagCollector;
    }

    public void run() {
        Comparator<ResourceLocation> resourceComparator = Comparator.comparing(ResourceLocation::toString);

        // Blocks
        Map<String, Map<String, Object>> blocks = new HashMap<>();
        IRegistry.field_212618_g.func_148742_b().stream().sorted(resourceComparator).forEach(blockId -> {
            Map<String, Object> bl = new HashMap<>();
            Block block = IRegistry.field_212618_g.func_82594_a(blockId);
            Map<String, List<Object>> properties = new HashMap<>();
            for(IProperty<?> prop : block.func_176194_O().func_177623_d()) {
                properties.put(prop.func_177701_a(), prop.func_177700_c().stream().map(s -> s.toString().toLowerCase()).collect(Collectors.toList()));
            }
            bl.put("properties", properties);
            StringBuilder defaultState = new StringBuilder();
            defaultState.append(blockId.toString());
            if (!block.func_176223_P().func_206871_b().isEmpty()) {
                List<String> bits = new ArrayList<>();
                block.func_176223_P().func_206871_b().forEach((prop, val) -> bits.add(prop.func_177701_a() + "=" + val.toString().toLowerCase()));
                defaultState.append("[").append(String.join(",", bits)).append("]");
            }
            bl.put("default", defaultState.toString());
            blocks.put(blockId.toString(), bl);
        });

        // Items
        List<String> items = IRegistry.field_212630_s.func_148742_b().stream().sorted(resourceComparator).map(ResourceLocation::toString).collect(Collectors.toList());

        // Entities
        List<String> entities = IRegistry.field_212629_r.func_148742_b().stream().sorted(resourceComparator).map(ResourceLocation::toString).collect(Collectors.toList());

        // Biomes
        List<String> biomes = IRegistry.field_212624_m.func_148742_b().stream().sorted(resourceComparator).map(ResourceLocation::toString).collect(Collectors.toList());

        // BlockTags
        final BlockTagsProvider blockTagsProvider = new BlockTagsProvider(null);
        ReflectionUtil.invokeMethod(blockTagsProvider, BlockTagsProvider.class, "func_200432_c", null, null); // initialize

        Map<String, List<String>> blockTags = getTags(blockTagsProvider, IRegistry.field_212618_g);

        // ItemTags
        final ItemTagsProvider itemTagsProvider = new ItemTagsProvider(null);
        ReflectionUtil.invokeMethod(itemTagsProvider, ItemTagsProvider.class, "func_200432_c", null, null); // initialize

        Map<String, List<String>> itemTags = getTags(itemTagsProvider, IRegistry.field_212630_s);

        Map<String, Object> output = new HashMap<>();
        output.put("blocks", blocks);
        output.put("items", items);
        output.put("entities", entities);
        output.put("biomes", biomes);
        output.put("blocktags", blockTags);
        output.put("itemtags", itemTags);
        output.put("entitytags", new HashMap<>());

        try {
            Files.write(gson.toJson(output), file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

