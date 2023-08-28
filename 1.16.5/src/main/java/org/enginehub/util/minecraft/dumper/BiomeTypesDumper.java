package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import com.squareup.javapoet.ClassName;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistry;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class BiomeTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BiomeTypesDumper().run();
    }

    public BiomeTypesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.biome", "BiomeType"), true);
    }

    @Override
    protected Iterator<String> getIds() {
        return getServerRegistry().registryOrThrow(Registry.BIOME_REGISTRY).keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "nether"
        );
    }
}
