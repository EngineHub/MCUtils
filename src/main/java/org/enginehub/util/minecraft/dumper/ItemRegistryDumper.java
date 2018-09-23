package org.enginehub.util.minecraft.dumper;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.util.text.translation.LanguageMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemRegistryDumper extends RegistryDumper<Item> {

    public static void main(String[] args) {
        setupGame();
        LanguageMap.func_74808_a();
        (new ItemRegistryDumper(new File("output/items.json"))).run();
    }

    public ItemRegistryDumper(File file) {
        super(file);
    }

    @Override
    public IRegistry<Item> getRegistry() {
        return IRegistry.field_212630_s;
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return new MapComparator();
    }

    public List<Map<String, Object>> getProperties(ResourceLocation resourceLocation, Item item) {
        List<Map<String, Object>> maps = new ArrayList<>();
        maps.add(getPropertiesForItem(resourceLocation, item));
        return maps;
    }

    private Map<String, Object> getPropertiesForItem(ResourceLocation resourceLocation, Item item) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", resourceLocation.toString());
        map.put("localizedName", item.func_200295_i(item.func_190903_i()).func_150261_e());
        return map;
    }

    private static class MapComparator implements Comparator<Map<String, Object>> {
        @Override
        public int compare(Map<String, Object> a, Map<String, Object> b) {
            return ((String) a.get("id")).compareTo((String) b.get("id"));
        }
    }
}

