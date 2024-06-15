package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
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
        super(
            Registries.BIOME,
            "com.sk89q.worldedit.world.biome", "Biome"
        );
    }

    @Override
    protected Iterator<ResourceLocation> getDeprecatedIds() {
        return Iterators.forArray(
            ResourceLocation.parse("nether"),
            ResourceLocation.parse("tall_birch_forest"),
            ResourceLocation.parse("giant_tree_taiga"),
            ResourceLocation.parse("giant_spruce_taiga"),
            ResourceLocation.parse("snowy_tundra"),
            ResourceLocation.parse("jungle_edge"),
            ResourceLocation.parse("stone_shore"),
            ResourceLocation.parse("mountains"),
            ResourceLocation.parse("wooded_mountains"),
            ResourceLocation.parse("gravelly_mountains"),
            ResourceLocation.parse("shattered_savanna"),
            ResourceLocation.parse("wooded_badlands_plateau"),
            ResourceLocation.parse("badlands_plateau"),
            ResourceLocation.parse("bamboo_jungle_hills"),
            ResourceLocation.parse("birch_forest_hills"),
            ResourceLocation.parse("dark_forest_hills"),
            ResourceLocation.parse("deep_warm_ocean"),
            ResourceLocation.parse("desert_hills"),
            ResourceLocation.parse("desert_lakes"),
            ResourceLocation.parse("giant_spruce_taiga_hills"),
            ResourceLocation.parse("giant_tree_taiga_hills"),
            ResourceLocation.parse("modified_gravelly_hills"),
            ResourceLocation.parse("modified_gravelly_mountains"),
            ResourceLocation.parse("jungle_hills"),
            ResourceLocation.parse("modified_badlands_plateau"),
            ResourceLocation.parse("modified_jungle"),
            ResourceLocation.parse("modified_jungle_edge"),
            ResourceLocation.parse("modified_wooded_badlands_plateau"),
            ResourceLocation.parse("mountain_edge"),
            ResourceLocation.parse("mushroom_field_shore"),
            ResourceLocation.parse("shattered_savanna_plateau"),
            ResourceLocation.parse("snowy_mountains"),
            ResourceLocation.parse("snowy_taiga_hills"),
            ResourceLocation.parse("snowy_taiga_mountains"),
            ResourceLocation.parse("swamp_hills"),
            ResourceLocation.parse("taiga_hills"),
            ResourceLocation.parse("taiga_mountains"),
            ResourceLocation.parse("tall_birch_hills"),
            ResourceLocation.parse("wooded_hills")
        );
    }
}

