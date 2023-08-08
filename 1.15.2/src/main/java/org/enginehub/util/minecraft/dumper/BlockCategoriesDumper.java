package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import com.squareup.javapoet.ClassName;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.Iterator;

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
    protected Iterator<String> getIds() {
        return BlockTags.getContainer().getKeys().stream().map(Identifier::getPath).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "dirt_like"
        );
    }
}
