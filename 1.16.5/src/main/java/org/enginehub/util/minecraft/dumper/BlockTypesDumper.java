package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerResources;
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
        return Registry.BLOCK.keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getTags() {
        return getServerResources().getTags().getBlocks().getAvailableTags().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "sign",
                "wall_sign"
        );
    }

    @Override
    protected Iterator<String> getDeprecatedTags() {
        return Iterators.forArray(
                "dirt_like"
        );
    }
}
