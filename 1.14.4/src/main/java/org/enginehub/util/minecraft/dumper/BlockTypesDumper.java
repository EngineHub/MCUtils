package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import com.squareup.javapoet.ClassName;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class BlockTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new BlockTypesDumper().run();
    }

    public BlockTypesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.block", "BlockType"), true);
    }

    @Override
    protected Iterator<String> getIds() {
        return Registry.BLOCK.keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "sign",
                "wall_sign"
        );
    }
}
