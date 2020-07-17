package org.enginehub.util.minecraft.dumper;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BiomeTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        (new BiomeTypesDumper(new File("output/biometypes.java"))).run();
    }

    public BiomeTypesDumper(File file) {
        super("BiomeType", file);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return Registry.BLOCK.getIds();
    }
}

