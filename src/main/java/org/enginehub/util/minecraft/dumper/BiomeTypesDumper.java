package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

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
    protected Iterator<Identifier> getDeprecatedIds() {
        return Iterators.forArray(
            Identifier.parse("nether"),
            Identifier.parse("tall_birch_forest"),
            Identifier.parse("giant_tree_taiga"),
            Identifier.parse("giant_spruce_taiga"),
            Identifier.parse("snowy_tundra"),
            Identifier.parse("jungle_edge"),
            Identifier.parse("stone_shore"),
            Identifier.parse("mountains"),
            Identifier.parse("wooded_mountains"),
            Identifier.parse("gravelly_mountains"),
            Identifier.parse("shattered_savanna"),
            Identifier.parse("wooded_badlands_plateau"),
            Identifier.parse("badlands_plateau"),
            Identifier.parse("bamboo_jungle_hills"),
            Identifier.parse("birch_forest_hills"),
            Identifier.parse("dark_forest_hills"),
            Identifier.parse("deep_warm_ocean"),
            Identifier.parse("desert_hills"),
            Identifier.parse("desert_lakes"),
            Identifier.parse("giant_spruce_taiga_hills"),
            Identifier.parse("giant_tree_taiga_hills"),
            Identifier.parse("modified_gravelly_hills"),
            Identifier.parse("modified_gravelly_mountains"),
            Identifier.parse("jungle_hills"),
            Identifier.parse("modified_badlands_plateau"),
            Identifier.parse("modified_jungle"),
            Identifier.parse("modified_jungle_edge"),
            Identifier.parse("modified_wooded_badlands_plateau"),
            Identifier.parse("mountain_edge"),
            Identifier.parse("mushroom_field_shore"),
            Identifier.parse("shattered_savanna_plateau"),
            Identifier.parse("snowy_mountains"),
            Identifier.parse("snowy_taiga_hills"),
            Identifier.parse("snowy_taiga_mountains"),
            Identifier.parse("swamp_hills"),
            Identifier.parse("taiga_hills"),
            Identifier.parse("taiga_mountains"),
            Identifier.parse("tall_birch_hills"),
            Identifier.parse("wooded_hills")
        );
    }

    @Override
    protected Iterator<Identifier> getDeprecatedTags() {
        return Iterators.forArray(
            Identifier.parse("has_closer_water_fog"),
            Identifier.parse("increased_fire_burnout"),
            Identifier.parse("plays_underwater_music"),
            Identifier.parse("snow_golem_melts"),
            Identifier.parse("without_patrol_spawns")
        );
    }
}

