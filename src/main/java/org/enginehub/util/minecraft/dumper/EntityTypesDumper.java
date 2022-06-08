package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.Registry;
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
            Registry.ENTITY_TYPE_REGISTRY,
            "com.sk89q.worldedit.world.entity", "Entity"
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedIds() {
        return Iterators.forArray(
            new ResourceLocation("zombie_pigman")
        );
    }
}
