package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.enginehub.util.minecraft.util.GameSetupUtils;

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
            new ItemRegistryDumper(new File("output/items.json")).run();
        }
    }

    public ItemRegistryDumper(File file) {
        super(file);
    }

    @Override
    public Registry<Item> getRegistry() {
        return GameSetupUtils.getServerRegistries().registryOrThrow(Registries.ITEM);
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return Comparator.comparing(map -> (String) map.get("id"));
    }

    public List<Map<String, Object>> getProperties(ResourceLocation resourceLocation, Item item) {
        List<Map<String, Object>> maps = new ArrayList<>();
        maps.add(getPropertiesForItem(resourceLocation, item));
        return maps;
    }

    private Map<String, Object> getPropertiesForItem(ResourceLocation resourceLocation, Item item) {
        Map<String, Object> map = new TreeMap<>();
        map.put("id", resourceLocation.toString());
        map.put("unlocalizedName", item.getDescriptionId(item.getDefaultInstance()));
        map.put("localizedName", item.getName(item.getDefaultInstance()).getString());
        map.put("maxDamage", item.components().get(DataComponents.MAX_DAMAGE));
        map.put("maxStackSize", item.components().get(DataComponents.MAX_STACK_SIZE));
        return map;
    }
}

