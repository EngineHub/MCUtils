package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistry;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
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
    protected Iterator<String> getIds() {
        return getServerRegistry().get(Registry.ENTITY_TYPE_KEY).getIds().stream().map(Identifier::getPath).iterator();
    }
}
