package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Bootstrap;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

public class BlockTypesDumper {

    public static void main(String[] args) {
        Bootstrap.func_151354_b();
        Block.func_149671_p();
        (new BlockTypesDumper(new File("output/blocktypes.java"))).run();
    }

    private File file;

    public BlockTypesDumper(File file) {
        this.file = file;
    }

    public void run() {
        StringBuilder builder = new StringBuilder();
        Set<ResourceLocation> resources = Sets.newTreeSet(Comparator.comparing(ResourceLocation::toString));
        resources.addAll(Block.field_149771_c.func_148742_b());
        for(ResourceLocation resourceLocation : resources) {
            String id = resourceLocation.toString();
            builder.append("public static final BlockType ")
                    .append(id.split(":")[1].toUpperCase())
                    .append(" = register(\"")
                    .append(id)
                    .append("\"");

            builder.append(");\n");
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

