package org.enginehub.util.minecraft.dumper;

import net.minecraft.util.Identifier;

import java.io.File;
import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BlockCategoriesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        (new BlockCategoriesDumper(new File("output/blockcategories.java"))).run();
    }

    public BlockCategoriesDumper(File file) {
        super("BlockCategory", file);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return loadServerResources().getRegistryTagManager().blocks().getKeys();
    }
}

