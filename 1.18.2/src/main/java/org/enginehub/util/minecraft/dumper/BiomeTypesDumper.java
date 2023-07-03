package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;

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
    protected Collection<String> getIds() {
        return getServerRegistry().get(Registry.BIOME_KEY).getIds().stream().map(Identifier::getPath).toList();
    }

    @Override
    protected Collection<String> getDeprecatedIds() {
        return ImmutableSet.of(
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
