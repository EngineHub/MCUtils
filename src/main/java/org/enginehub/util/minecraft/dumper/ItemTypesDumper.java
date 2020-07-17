package org.enginehub.util.minecraft.dumper;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class ItemTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        (new ItemTypesDumper(new File("output/itemtypes.java"))).run();
    }

    public ItemTypesDumper(File file) {
        super("ItemType", file);
    }
    @Override
    protected Collection<Identifier> getIds() {
        return Registry.ITEM.getIds();
    }
}
