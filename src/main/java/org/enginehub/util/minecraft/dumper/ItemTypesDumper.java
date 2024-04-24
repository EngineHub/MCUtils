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
            new ResourceLocation("cactus_green"),
            new ResourceLocation("dandelion_yellow"),
            new ResourceLocation("rose_red"),
            new ResourceLocation("sign"),
            new ResourceLocation("zombie_pigman_spawn_egg"),
            new ResourceLocation("grass_path"),
            new ResourceLocation("grass"),
            new ResourceLocation("scute")
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedTags() {
        return Iterators.forArray(
            new ResourceLocation("carpets"),
            new ResourceLocation("furnace_materials"),
            new ResourceLocation("occludes_vibration_signals"),
            new ResourceLocation("tools"),
            new ResourceLocation("overworld_natural_logs"),
            new ResourceLocation("axolotl_tempt_items")
        );
    }
}
