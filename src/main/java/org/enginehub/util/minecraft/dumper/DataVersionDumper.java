package org.enginehub.util.minecraft.dumper;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.IntegerProperty;
import net.minecraft.tags.ITag;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.registry.Registry;
import org.enginehub.util.minecraft.util.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DataVersionDumper {

    public static void main(String[] args) {
        setupGame();
        (new DataVersionDumper(new File("output/" + SharedConstants.func_215069_a().getWorldVersion() + ".json"))).run();
    }

    private File file;
    private Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public DataVersionDumper(File file) {
        this.file = file;
    }

    @SuppressWarnings("unchecked")
    private <T> Map<String, List<String>> getTags(TagsProvider<T> provider, Registry<T> registry) {
        Map<String, List<String>> tagCollector = new HashMap<>();

        Map<ResourceLocation, net.minecraft.tags.ITag.Builder> tags = (Map<ResourceLocation, net.minecraft.tags.ITag.Builder>) ReflectionUtil.getField(provider, TagsProvider.class, "field_200434_b");

        Map<String, List<Tag.TagEntry>> deferred = new LinkedHashMap<>();
        tags.forEach((key, value) -> {
            ((List<Tag.Proxy>) ReflectionUtil.getField(value, Tag.Builder.class, "field_232953_a_")).forEach(proxy -> {
                ITag.ITagEntry entry = proxy.func_232968_a_();
                if (entry instanceof Tag.TagEntry) {
                    deferred.computeIfAbsent(key.toString(), kk -> new ArrayList<>()).add((Tag.TagEntry) entry);
                } else {
                    tagCollector.computeIfAbsent(key.toString(), kk -> new ArrayList<>()).add(
                            ReflectionUtil.getField(entry, ITag.ItemEntry.class, "field_232969_a_").toString());
                }
            });
        });

        while (!deferred.isEmpty()) {
            Map<String, List<Tag.TagEntry>> iter = new LinkedHashMap<>(deferred);
            deferred.clear();
            iter.forEach((key, value) -> {
                for (Tag.TagEntry entry : value) {
                    String name = ReflectionUtil.getField(entry, ITag.TagEntry.class, "field_200163_a").toString();
                    if (deferred.containsKey(name)) {
                        deferred.put(key, value);
                    } else if (!tagCollector.containsKey(name)) {
                        throw new RuntimeException("Missing tag! " + name);
                    } else {
                        tagCollector.computeIfAbsent(key, kk -> new ArrayList<>()).addAll(tagCollector.get(name));
                    }
                }
            });
        }

        return tagCollector;
    }

    private String getTypeName(Class<? extends Property> clazz) {
        if (clazz == EnumProperty.class) {
            return "enum";
        } else if (clazz == IntegerProperty.class) {
            return "int";
        } else if (clazz == BooleanProperty.class) {
            return "bool";
        } else if (clazz == DirectionProperty.class) {
            return "direction";
        } else {
            throw new RuntimeException("Unknown property!");
        }
    }

    public void run() {
        Comparator<ResourceLocation> resourceComparator = Comparator.comparing(ResourceLocation::toString);

        // Blocks
        Map<String, Map<String, Object>> blocks = new LinkedHashMap<>();
        Registry.field_212618_g.func_148742_b().stream().sorted(resourceComparator).forEach(blockId -> {
            Map<String, Object> bl = new LinkedHashMap<>();
            Block block = Registry.field_212618_g.func_82594_a(blockId);
            Map<String, Object> properties = new LinkedHashMap<>();
            for(Property<?> prop : block.func_176194_O().func_177623_d()) {
                Map<String, Object> propertyValues = new LinkedHashMap<>();
                propertyValues.put("values", prop.func_177700_c().stream().map(s -> s.toString().toLowerCase()).collect(Collectors.toList()));
                propertyValues.put("type", getTypeName(prop.getClass()));
                properties.put(prop.func_177701_a(), propertyValues);
            }
            bl.put("properties", properties);
            StringBuilder defaultState = new StringBuilder();
            defaultState.append(blockId.toString());
            if (!block.func_176223_P().func_206871_b().isEmpty()) {
                List<String> bits = new ArrayList<>();
                block.func_176223_P().func_206871_b().forEach((prop, val) -> bits.add(prop.func_177701_a() + "=" + val.toString().toLowerCase()));
                defaultState.append("[").append(String.join(",", bits)).append("]");
            }
            bl.put("defaultstate", defaultState.toString());
            blocks.put(blockId.toString(), bl);
        });

        // Items
        List<String> items = Registry.field_212630_s.func_148742_b().stream().sorted(resourceComparator).map(ResourceLocation::toString).collect(Collectors.toList());

        // Entities
        List<String> entities = Registry.field_212629_r.func_148742_b().stream().sorted(resourceComparator).map(ResourceLocation::toString).collect(Collectors.toList());

        // Biomes
        List<String> biomes = Registry.field_212624_m.func_148742_b().stream().sorted(resourceComparator).map(ResourceLocation::toString).collect(Collectors.toList());

        // BlockTags
        final BlockTagsProvider blockTagsProvider = new BlockTagsProvider(null);
        ReflectionUtil.invokeMethod(blockTagsProvider, BlockTagsProvider.class, "func_200432_c", null, null); // initialize

        Map<String, List<String>> blockTags = getTags(blockTagsProvider, Registry.field_212618_g);

        // ItemTags
        final ItemTagsProvider itemTagsProvider = new ItemTagsProvider(null, blockTagsProvider);
        ReflectionUtil.invokeMethod(itemTagsProvider, ItemTagsProvider.class, "func_200432_c", null, null); // initialize

        Map<String, List<String>> itemTags = getTags(itemTagsProvider, Registry.field_212630_s);

        // EntityTags
        final EntityTypeTagsProvider entityTypeTagsProvider = new EntityTypeTagsProvider(null);
        ReflectionUtil.invokeMethod(entityTypeTagsProvider, EntityTypeTagsProvider.class, "func_200432_c", null, null); // initialize

        Map<String, List<String>> entityTags = getTags(entityTypeTagsProvider, Registry.field_212629_r);

        Map<String, Object> output = new LinkedHashMap<>();
        output.put("blocks", blocks);
        output.put("items", items);
        output.put("entities", entities);
        output.put("biomes", biomes);
        output.put("blocktags", blockTags);
        output.put("itemtags", itemTags);
        output.put("entitytags", entityTags);

        try {
            Files.write(gson.toJson(output), file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

