package org.enginehub.util.minecraft.dumper;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        resources.addAll(Block.field_149771_c.func_148742_b());
        for(ResourceLocation resourceLocation : resources) {
            String id = resourceLocation.toString();
            builder.append("public static final BlockType ")
                    .append(id.split(":")[1].toUpperCase())
                    .append(" = register(\"")
                    .append(id)
                    .append("\"");

            Block block = Block.field_149771_c.func_82594_a(resourceLocation);
            List<String> withs = new ArrayList<>();
            for (Map.Entry<IProperty<?>, Comparable<?>> entry : block.func_176223_P().func_206871_b().entrySet()) {
                String valueString;
                if (entry.getKey() instanceof IntegerProperty || entry.getKey() instanceof BooleanProperty) {
                    valueString = String.valueOf(entry.getValue());
                } else if (entry.getKey() instanceof DirectionProperty) {
                    valueString = "Direction." + String.valueOf(entry.getValue()).toUpperCase();
                } else if (entry.getKey() instanceof EnumProperty) {
                    valueString = "\"" + String.valueOf(entry.getValue()) + "\"";
                } else {
                    continue;
                }
                withs.add(".with(state.getBlockType().getProperty(\"" + entry.getKey().func_177701_a() + "\"), " + valueString + ")");
            }
            if (!withs.isEmpty()) {
                builder.append(", state -> state");
                for (String with : withs) {
                    builder.append(with);
                }
            }

            builder.append(");\n");
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

