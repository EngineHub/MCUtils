package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.enginehub.util.minecraft.RunAll;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public Registry<Item> getRegistry() {
        return Registry.ITEM;
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return Comparator.comparing(map -> (String) map.get("id"));
    }

    public List<Map<String, Object>> getProperties(Identifier resourceLocation, Item item) {
        List<Map<String, Object>> maps = new ArrayList<>();
        maps.add(getPropertiesForItem(resourceLocation, item));
        return maps;
    }

    private Map<String, Object> getPropertiesForItem(Identifier resourceLocation, Item item) {
        Map<String, Object> map = new TreeMap<>();
        map.put("id", resourceLocation.toString());
        map.put("unlocalizedName", item.getTranslationKey(item.getDefaultStack()));
        map.put("localizedName", item.getName(item.getDefaultStack()).getString());
        map.put("maxDamage", item.getMaxDamage());
        map.put("maxStackSize", item.getMaxCount());
        return map;
    }
}
