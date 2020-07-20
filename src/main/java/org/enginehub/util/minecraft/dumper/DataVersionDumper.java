package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.block.Block;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.tag.RegistryTagManager;
import net.minecraft.tag.TagContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class DataVersionDumper implements Dumper {

    public static void main(String[] args) {
        setupGame();
        new Default().run();
    }

    @AutoService(Dumper.class)
    public static class Default implements Dumper {
        @Override
        public void run() {
            File file = new File("output/" + SharedConstants.getGameVersion().getWorldVersion() + ".json");
            new DataVersionDumper(file).run();
        }
    }

    private final File file;
    private final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public DataVersionDumper(File file) {
        this.file = file;
    }

    private <T> Map<String, List<String>> getTags(TagContainer<T> provider, Registry<T> registry) {
        Map<String, List<String>> tagCollector = new TreeMap<>();

        provider.getEntries().forEach((key, value) ->
            tagCollector.put(key.toString(), value.values().stream()
                .map(entry -> checkNotNull(registry.getId(entry)))
                .map(Identifier::toString)
                .sorted()
                .collect(Collectors.toList())));

        return tagCollector;
    }

    @SuppressWarnings("rawtypes")
    private String getTypeName(Class<? extends Property> clazz) {
        if (clazz == EnumProperty.class) {
            return "enum";
        } else if (clazz == IntProperty.class) {
            return "int";
        } else if (clazz == BooleanProperty.class) {
            return "bool";
        } else if (clazz == DirectionProperty.class) {
            return "direction";
        } else {
            throw new RuntimeException("Unknown property!");
        }
    }

    @Override
    public void run() {
        // Blocks
        Map<String, Map<String, Object>> blocks = new TreeMap<>();
        for (Identifier blockId : Registry.BLOCK.getIds()) {
            Map<String, Object> bl = new TreeMap<>();
            Block block = Registry.BLOCK.get(blockId);
            Map<String, Object> properties = new TreeMap<>();
            for (Property<?> prop : block.getDefaultState().getProperties()) {
                Map<String, Object> propertyValues = new TreeMap<>();
                propertyValues.put("values", prop.getValues().stream().map(s -> s.toString().toLowerCase()).collect(Collectors.toList()));
                propertyValues.put("type", getTypeName(prop.getClass()));
                properties.put(prop.getName(), propertyValues);
            }
            bl.put("properties", properties);
            StringBuilder defaultState = new StringBuilder();
            defaultState.append(blockId.toString());
            if (!block.getDefaultState().getEntries().isEmpty()) {
                List<String> bits = new ArrayList<>();
                block.getDefaultState().getEntries().entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getKey().getName()))
                    .forEach(e ->
                        bits.add(e.getKey().getName() + "=" + e.getValue().toString().toLowerCase())
                    );
                defaultState.append("[").append(String.join(",", bits)).append("]");
            }
            bl.put("defaultstate", defaultState.toString());
            blocks.put(blockId.toString(), bl);
        }

        // Items
        List<String> items = Registry.ITEM.getIds().stream().sorted().map(Identifier::toString).collect(Collectors.toList());

        // Entities
        List<String> entities = Registry.ENTITY_TYPE.getIds().stream().sorted().map(Identifier::toString).collect(Collectors.toList());

        // Biomes
        List<String> biomes = Registry.BIOME.getIds().stream().sorted().map(Identifier::toString).collect(Collectors.toList());

        RegistryTagManager tagManager = getServerResources().getRegistryTagManager();
        // BlockTags
        Map<String, List<String>> blockTags = getTags(tagManager.blocks(), Registry.BLOCK);

        // ItemTags
        Map<String, List<String>> itemTags = getTags(tagManager.items(), Registry.ITEM);

        // EntityTags
        Map<String, List<String>> entityTags = getTags(tagManager.entityTypes(), Registry.ENTITY_TYPE);

        Map<String, Object> output = new TreeMap<>();
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

