package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistries;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class EntityTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new EntityTypesDumper().run();
    }

    public EntityTypesDumper() {
        super("com.sk89q.worldedit.world.entity", "Entity");
    }

    @Override
    protected Iterator<String> getIds() {
        return getServerRegistries().registryOrThrow(Registries.ENTITY_TYPE).keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getTags() {
        return getServerRegistries().registryOrThrow(Registries.ENTITY_TYPE).getTagNames().map(tagKey -> tagKey.location().getPath()).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "zombie_pigman"
        );
    }
}
