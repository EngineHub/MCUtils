package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class ItemTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new ItemTypesDumper().run();
    }

    public ItemTypesDumper() {
        super(
            Registries.ITEM,
            "com.sk89q.worldedit.world.item", "Item"
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedIds() {
        return Iterators.forArray(
            ResourceLocation.parse("cactus_green"),
            ResourceLocation.parse("dandelion_yellow"),
            ResourceLocation.parse("rose_red"),
            ResourceLocation.parse("sign"),
            ResourceLocation.parse("zombie_pigman_spawn_egg"),
            ResourceLocation.parse("grass_path"),
            ResourceLocation.parse("grass"),
            ResourceLocation.parse("scute")
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedTags() {
        return Iterators.forArray(
            ResourceLocation.parse("carpets"),
            ResourceLocation.parse("furnace_materials"),
            ResourceLocation.parse("occludes_vibration_signals"),
            ResourceLocation.parse("tools"),
            ResourceLocation.parse("overworld_natural_logs"),
            ResourceLocation.parse("axolotl_tempt_items")
        );
    }
}
