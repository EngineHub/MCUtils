package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.ImmutableSortedSet;
import net.minecraft.block.Block;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BlockCategoriesDumper {

    public static void main(String[] args) {
        setupGame();
        (new BlockCategoriesDumper(new File("output/blockcategories.java"))).run();
    }

    private final File file;

    public BlockCategoriesDumper(File file) {
        this.file = file;
    }

    private void run() {
        RegistryTagContainer<Block> blockTags = loadServerResources().getRegistryTagManager().blocks();
        Set<Identifier> resources = ImmutableSortedSet.copyOf(
            Comparator.comparing(Identifier::toString),
            blockTags.getKeys()
        );

        StringBuilder builder = new StringBuilder();
        for (Identifier resourceLocation : resources) {
            builder.append("public static final BlockCategory ")
                    .append(resourceLocation.getPath().toUpperCase())
                    .append(" = get(\"")
                    .append(resourceLocation.toString())
                    .append("\");\n");
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

