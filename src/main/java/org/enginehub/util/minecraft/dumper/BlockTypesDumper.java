package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.Sets;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BlockTypesDumper {

    public static void main(String[] args) {
        setupGame();
        (new BlockTypesDumper(new File("output/blocktypes.java"))).run();
    }

    private File file;

    public BlockTypesDumper(File file) {
        this.file = file;
    }

    public void run() {
        StringBuilder builder = new StringBuilder();
        Set<ResourceLocation> resources = Sets.newTreeSet(Comparator.comparing(ResourceLocation::toString));
        resources.addAll(Registry.field_212618_g.func_148742_b());
        for(ResourceLocation resourceLocation : resources) {
            String id = resourceLocation.toString();
            builder.append("@Nullable public static final BlockType ")
                    .append(id.split(":")[1].toUpperCase())
                    .append(" = get(\"")
                    .append(id)
                    .append("\");\n");
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

