package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;

import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BlockCategoriesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BlockCategoriesDumper().run();
    }

    public BlockCategoriesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.block", "BlockCategory"), false);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return loadServerResources().getRegistryTagManager().blocks().getKeys();
    }

    @Override
    protected Collection<Identifier> getDeprecatedIds() {
        return ImmutableSet.of(
            new Identifier("minecraft", "dirt_like")
        );
    }
}

