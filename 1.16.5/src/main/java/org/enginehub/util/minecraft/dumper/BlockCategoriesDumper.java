package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;

import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class BlockCategoriesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BlockCategoriesDumper().run();
    }

    public BlockCategoriesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.block", "BlockCategory"), false);
    }

    @Override
    protected Collection<String> getIds() {
        return getServerResources().getRegistryTagManager().getBlocks().getTagIds().stream().map(Identifier::getPath).toList();
    }

    @Override
    protected Collection<String> getDeprecatedIds() {
        return ImmutableSet.of(
            "dirt_like"
        );
    }
}
