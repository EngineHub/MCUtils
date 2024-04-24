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
            new ResourceLocation("sign"),
            new ResourceLocation("wall_sign"),
            new ResourceLocation("grass_path"),
            new ResourceLocation("grass")
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedTags() {
        return Iterators.forArray(
            new ResourceLocation("dirt_like"),
            new ResourceLocation("carpets"),
            new ResourceLocation("lava_pool_stone_replaceables"),
            new ResourceLocation("replaceable_plants"),
            new ResourceLocation("non_flammable_wood")
        );
    }
}

