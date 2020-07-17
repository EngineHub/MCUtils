package org.enginehub.util.minecraft.dumper;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class EntityTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        (new EntityTypesDumper(new File("output/entitytypes.java"))).run();
    }

    public EntityTypesDumper(File file) {
        super("EntityType", file);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return Registry.ENTITY_TYPE.getIds();
    }
}
