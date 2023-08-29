package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

// TODO Get the 1.14.4 and 1.15.2 Category dumpers to work
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
        return BlockTags.getAllTags().getAvailableTags().stream().map(ResourceLocation::getPath).iterator();
    }
}
