package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.Sets;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

public class ItemTypesDumper {

    public static void main(String[] args) {
        Bootstrap.func_151354_b();
        Item.func_150900_l();
        Item.func_150900_l();
        (new ItemTypesDumper(new File("output/itemtypes.java"))).run();
    }

    private File file;

    public ItemTypesDumper(File file) {
        this.file = file;
    }

    public void run() {
        StringBuilder builder = new StringBuilder();
        Set<ResourceLocation> resources = Sets.newTreeSet(Comparator.comparing(ResourceLocation::toString));
        resources.addAll(Item.field_150901_e.func_148742_b());
        for(ResourceLocation resourceLocation : resources) {
            String id = resourceLocation.toString();
            builder.append("public static final ItemType ")
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
