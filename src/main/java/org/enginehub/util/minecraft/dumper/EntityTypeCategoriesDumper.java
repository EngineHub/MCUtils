package org.enginehub.util.minecraft.dumper;

import net.minecraft.util.Identifier;

import java.io.File;
import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class EntityTypeCategoriesDumper extends RegistryClassDumper {

    // worldedit doesn't actually do entity type categories yet
    public static void main(String[] args) {
        setupGame();
        (new EntityTypeCategoriesDumper(new File("output/entitytypecategories.java"))).run();
    }

    public EntityTypeCategoriesDumper(File file) {
        super("EntityTypeCategory", file);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return loadServerResources().getRegistryTagManager().entityTypes().getKeys();
    }
}

