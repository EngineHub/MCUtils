package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
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
        super("com.sk89q.worldedit.world.biome", "Biome");
    }

    @Override
    protected Iterator<String> getIds() {
        return getServerRegistry().registryOrThrow(Registry.BIOME_REGISTRY).keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getTags() {
        return getServerRegistry().registryOrThrow(Registry.BIOME_REGISTRY).getTagNames().map(tagKey -> tagKey.location().getPath()).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "nether",
                "tall_birch_forest",
                "giant_tree_taiga",
                "giant_spruce_taiga",
                "snowy_tundra",
                "jungle_edge",
                "stone_shore",
                "mountains",
                "wooded_mountains",
                "gravelly_mountains",
                "shattered_savanna",
                "wooded_badlands_plateau",
                "badlands_plateau",
                "bamboo_jungle_hills",
                "birch_forest_hills",
                "dark_forest_hills",
                "deep_warm_ocean",
                "desert_hills",
                "desert_lakes",
                "giant_spruce_taiga_hills",
                "giant_tree_taiga_hills",
                "modified_gravelly_hills",
                "jungle_hills",
                "modified_badlands_plateau",
                "modified_jungle",
                "modified_jungle_edge",
                "modified_wooded_badlands_plateau",
                "mountain_edge",
                "mushroom_field_shore",
                "shattered_savanna_plateau",
                "snowy_mountains",
                "snowy_taiga_hills",
                "snowy_taiga_mountains",
                "swamp_hills",
                "taiga_hills",
                "taiga_mountains",
                "tall_birch_hills",
                "wooded_hills"
        );
    }
}
