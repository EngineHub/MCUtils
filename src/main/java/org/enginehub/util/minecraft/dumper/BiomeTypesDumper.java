package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BiomeTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BiomeTypesDumper().run();
    }

    public BiomeTypesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.biome", "BiomeType"), true);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return Registry.BIOME.getIds();
    }

    @Override
    protected Collection<Identifier> getDeprecatedIds() {
        return ImmutableSet.of(
            new Identifier("minecraft", "nether")
        );
    }
}

