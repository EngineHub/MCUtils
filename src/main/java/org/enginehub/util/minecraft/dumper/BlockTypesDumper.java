package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

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
    protected Iterator<Identifier> getDeprecatedIds() {
        return Iterators.forArray(
            Identifier.parse("sign"),
            Identifier.parse("wall_sign"),
            Identifier.parse("grass_path"),
            Identifier.parse("grass"),
            Identifier.parse("chain")
        );
    }

    @Override
    protected Iterator<Identifier> getDeprecatedTags() {
        return Iterators.forArray(
            Identifier.parse("dirt_like"),
            Identifier.parse("carpets"),
            Identifier.parse("lava_pool_stone_replaceables"),
            Identifier.parse("replaceable_plants"),
            Identifier.parse("non_flammable_wood"),
            Identifier.parse("tall_flowers"),
            Identifier.parse("dead_bush_may_place_on"),
            Identifier.parse("plays_ambient_desert_block_sounds")
        );
    }
}

