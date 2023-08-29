package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

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
        return Registry.BIOME.keySet().stream().map(ResourceLocation::getPath).iterator();
    }
}
