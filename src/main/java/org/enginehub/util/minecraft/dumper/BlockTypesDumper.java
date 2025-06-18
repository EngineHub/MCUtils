package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class BlockTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BlockTypesDumper().run();
    }

    public BlockTypesDumper() {
        super(
            Registries.BLOCK,
            "com.sk89q.worldedit.world.block", "Block"
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedIds() {
        return Iterators.forArray(
            ResourceLocation.parse("sign"),
            ResourceLocation.parse("wall_sign"),
            ResourceLocation.parse("grass_path"),
            ResourceLocation.parse("grass")
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedTags() {
        return Iterators.forArray(
            ResourceLocation.parse("dirt_like"),
            ResourceLocation.parse("carpets"),
            ResourceLocation.parse("lava_pool_stone_replaceables"),
            ResourceLocation.parse("replaceable_plants"),
            ResourceLocation.parse("non_flammable_wood"),
            ResourceLocation.parse("tall_flowers"),
            ResourceLocation.parse("dead_bush_may_place_on"),
            ResourceLocation.parse("plays_ambient_desert_block_sounds")
        );
    }
}

