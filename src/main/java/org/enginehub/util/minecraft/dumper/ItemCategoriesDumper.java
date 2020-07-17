package org.enginehub.util.minecraft.dumper;

import net.minecraft.util.Identifier;

import java.io.File;
import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class ItemCategoriesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        (new ItemCategoriesDumper(new File("output/itemcategories.java"))).run();
    }

    public ItemCategoriesDumper(File file) {
        super("ItemCategory", file);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return loadServerResources().getRegistryTagManager().items().getKeys();
    }
}

