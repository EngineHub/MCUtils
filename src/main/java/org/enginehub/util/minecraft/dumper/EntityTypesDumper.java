package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class EntityTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new EntityTypesDumper().run();
    }

    public EntityTypesDumper() {
        super(
            Registries.ENTITY_TYPE,
            "com.sk89q.worldedit.world.entity", "Entity"
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedIds() {
        return Iterators.forArray(
            ResourceLocation.parse("zombie_pigman"),
            ResourceLocation.parse("boat"),
            ResourceLocation.parse("chest_boat"),
            ResourceLocation.parse("creaking_transient"),
            ResourceLocation.parse("potion")
        );
    }
}
