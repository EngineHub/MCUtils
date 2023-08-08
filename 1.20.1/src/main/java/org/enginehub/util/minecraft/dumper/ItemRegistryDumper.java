package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.io.File;
import java.util.*;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistries;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class ItemRegistryDumper extends RegistryDumper<Item> {

    public static void main(String[] args) {
        setupGame();
        new Default().run();
    }

    @AutoService(Dumper.class)
    public static class Default implements Dumper {
        @Override
        public void run() {
            new ItemRegistryDumper(new File(OUTPUT, "items.json")).run();
        }
    }

    public ItemRegistryDumper(File file) {
        super(file);
    }

    @Override
    public Collection<String> getIds() {
        return getServerRegistries().registryOrThrow(Registries.ITEM).keySet().stream().map(ResourceLocation::toString).toList();
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return Comparator.comparing(map -> (String) map.get("id"));
    }

    @Override
    public List<Map<String, Object>> getProperties(String resourceLocation) {
        Item item = getServerRegistries().registryOrThrow(Registries.ITEM).get(new ResourceLocation(resourceLocation));
        List<Map<String, Object>> maps = new ArrayList<>();
        maps.add(getPropertiesForItem(resourceLocation, item));
        return maps;
    }

    private Map<String, Object> getPropertiesForItem(String resourceLocation, Item item) {
        Map<String, Object> map = new TreeMap<>();
        map.put("id", resourceLocation);
        map.put("unlocalizedName", item.getDescriptionId(item.getDefaultInstance()));
        map.put("localizedName", item.getName(item.getDefaultInstance()).getString());
        map.put("maxDamage", item.getMaxDamage());
        map.put("maxStackSize", item.getMaxStackSize());
        return map;
    }
}
