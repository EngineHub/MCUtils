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
    protected Collection<Identifier> getIds() {
        return getServerRegistry().get(Registry.BIOME_KEY).getIds();
    }

    @Override
    protected Collection<Identifier> getDeprecatedIds() {
        return ImmutableSet.of(
                new Identifier("nether"),
                new Identifier("badlands_plateau"),
                new Identifier("bamboo_jungle_hills"),
                new Identifier("birch_forest_hills"),
                new Identifier("dark_forest_hills"),
                new Identifier("deep_warm_ocean"),
                new Identifier("desert_hills"),
                new Identifier("desert_lakes"),
                new Identifier("giant_spruce_taiga_hills"),
                new Identifier("giant_tree_taiga_hills"),
                new Identifier("modified_gravelly_hills"),
                new Identifier("jungle_hills"),
                new Identifier("modified_badlands_plateau"),
                new Identifier("modified_jungle"),
                new Identifier("modified_jungle_edge"),
                new Identifier("modified_wooded_badlands_plateau"),
                new Identifier("mountain_edge"),
                new Identifier("mushroom_field_shore"),
                new Identifier("shattered_savanna_plateau"),
                new Identifier("snowy_mountains"),
                new Identifier("snowy_taiga_hills"),
                new Identifier("snowy_taiga_mountains"),
                new Identifier("swamp_hills"),
                new Identifier("taiga_hills"),
                new Identifier("taiga_mountains"),
                new Identifier("tall_birch_hills"),
                new Identifier("wooded_hills")
        );
    }
}
