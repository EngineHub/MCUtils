package org.enginehub.util.minecraft.dumper;

import net.minecraft.block.Block;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.text.translation.LanguageMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ItemRegistryDumper extends RegistryDumper<Item> {

    public static void main(String[] args) {
        Bootstrap.func_151354_b();
        Block.func_149671_p();
        Item.func_150900_l();
        (new ItemRegistryDumper(new File("output/items.json"))).run();
    }

    public ItemRegistryDumper(File file) {
        super(file);
    }

    @Override
    public RegistryNamespaced<ResourceLocation, Item> getRegistry() {
        return Item.field_150901_e;
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
        map.put("localizedName", LanguageMap.func_74808_a().func_74805_b(item.func_77658_a()));
        return map;
    }

    private static class MapComparator implements Comparator<Map<String, Object>> {
        @Override
        public int compare(Map<String, Object> a, Map<String, Object> b) {
            return ((String) a.get("id")).compareTo((String) b.get("id"));
        }
    }
}

