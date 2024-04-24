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
            new ResourceLocation("nether"),
            new ResourceLocation("tall_birch_forest"),
            new ResourceLocation("giant_tree_taiga"),
            new ResourceLocation("giant_spruce_taiga"),
            new ResourceLocation("snowy_tundra"),
            new ResourceLocation("jungle_edge"),
            new ResourceLocation("stone_shore"),
            new ResourceLocation("mountains"),
            new ResourceLocation("wooded_mountains"),
            new ResourceLocation("gravelly_mountains"),
            new ResourceLocation("shattered_savanna"),
            new ResourceLocation("wooded_badlands_plateau"),
            new ResourceLocation("badlands_plateau"),
            new ResourceLocation("bamboo_jungle_hills"),
            new ResourceLocation("birch_forest_hills"),
            new ResourceLocation("dark_forest_hills"),
            new ResourceLocation("deep_warm_ocean"),
            new ResourceLocation("desert_hills"),
            new ResourceLocation("desert_lakes"),
            new ResourceLocation("giant_spruce_taiga_hills"),
            new ResourceLocation("giant_tree_taiga_hills"),
            new ResourceLocation("modified_gravelly_hills"),
            new ResourceLocation("modified_gravelly_mountains"),
            new ResourceLocation("jungle_hills"),
            new ResourceLocation("modified_badlands_plateau"),
            new ResourceLocation("modified_jungle"),
            new ResourceLocation("modified_jungle_edge"),
            new ResourceLocation("modified_wooded_badlands_plateau"),
            new ResourceLocation("mountain_edge"),
            new ResourceLocation("mushroom_field_shore"),
            new ResourceLocation("shattered_savanna_plateau"),
            new ResourceLocation("snowy_mountains"),
            new ResourceLocation("snowy_taiga_hills"),
            new ResourceLocation("snowy_taiga_mountains"),
            new ResourceLocation("swamp_hills"),
            new ResourceLocation("taiga_hills"),
            new ResourceLocation("taiga_mountains"),
            new ResourceLocation("tall_birch_hills"),
            new ResourceLocation("wooded_hills")
        );
    }
}

