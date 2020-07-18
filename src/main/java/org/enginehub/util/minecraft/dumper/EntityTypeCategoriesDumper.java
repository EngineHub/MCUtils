package org.enginehub.util.minecraft.dumper;

import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;

import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class EntityTypeCategoriesDumper extends RegistryClassDumper {

    // worldedit doesn't actually do entity type categories yet
    public static void main(String[] args) {
        setupGame();
        new EntityTypeCategoriesDumper().run();
    }

    public EntityTypeCategoriesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.entity", "EntityTypeCategory"), false);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return loadServerResources().getRegistryTagManager().entityTypes().getKeys();
    }
}

