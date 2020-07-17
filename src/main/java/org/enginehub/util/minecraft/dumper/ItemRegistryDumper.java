package org.enginehub.util.minecraft.dumper;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class ItemRegistryDumper extends RegistryDumper<Item> {

    public static void main(String[] args) {
        setupGame();
        (new ItemRegistryDumper(new File("output/items.json"))).run();
    }

    public ItemRegistryDumper(File file) {
        super(file);
    }

    @Override
    public Registry<Item> getRegistry() {
        return Registry.ITEM;
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return new MapComparator();
    }

    public List<Map<String, Object>> getProperties(Identifier resourceLocation, Item item) {
        List<Map<String, Object>> maps = new ArrayList<>();
        maps.add(getPropertiesForItem(resourceLocation, item));
        return maps;
    }

    private Map<String, Object> getPropertiesForItem(Identifier resourceLocation, Item item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", resourceLocation.toString());
        map.put("unlocalizedName", item.getTranslationKey(item.getStackForRender()));
        map.put("localizedName", item.getName(item.getStackForRender()).getString());
        map.put("maxDamage", item.getMaxDamage());
        map.put("maxStackSize", item.getMaxCount());
        return map;
    }

    private static class MapComparator implements Comparator<Map<String, Object>> {
        @Override
        public int compare(Map<String, Object> a, Map<String, Object> b) {
            return ((String) a.get("id")).compareTo((String) b.get("id"));
        }
    }
}

