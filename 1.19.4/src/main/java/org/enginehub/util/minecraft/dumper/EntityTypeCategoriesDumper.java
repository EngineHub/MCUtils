package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import net.minecraft.core.registries.Registries;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistries;
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
        return getServerRegistries().registryOrThrow(Registries.ENTITY_TYPE).getTagNames().map(tagKey -> tagKey.location().getPath()).iterator();
    }
}
