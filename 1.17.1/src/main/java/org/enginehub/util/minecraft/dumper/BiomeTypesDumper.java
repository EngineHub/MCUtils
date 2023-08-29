package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.*;

@AutoService(Dumper.class)
public class BiomeTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BiomeTypesDumper().run();
    }

    public BiomeTypesDumper() {
        super("com.sk89q.worldedit.world.biome", "Biome");
    }

    @Override
    protected Iterator<String> getIds() {
        return getServerRegistry().registryOrThrow(Registry.BIOME_REGISTRY).keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getTags() {
        return getServerResources().getTags().getOrEmpty(Registry.BIOME_REGISTRY).getAvailableTags().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "nether"
        );
    }
}
