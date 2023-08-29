package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistries;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class BlockTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BlockTypesDumper().run();
    }

    public BlockTypesDumper() {
        super("com.sk89q.worldedit.world.block", "Block");
    }

    @Override
    protected Iterator<String> getIds() {
        return getServerRegistries().registryOrThrow(Registries.BLOCK).keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getTags() {
        return getServerRegistries().registryOrThrow(Registries.BLOCK).getTagNames().map(tagKey -> tagKey.location().getPath()).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "sign",
                "wall_sign",
                "grass_path"
        );
    }

    @Override
    protected Iterator<String> getDeprecatedTags() {
        return Iterators.forArray(
                "dirt_like",
                "carpets",
                "lava_pool_stone_replaceables"
        );
    }
}
