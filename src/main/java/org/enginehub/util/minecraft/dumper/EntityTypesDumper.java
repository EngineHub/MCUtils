package org.enginehub.util.minecraft.dumper;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

public class EntityTypesDumper {

    public static void main(String[] args) {
        setupGame();
        (new EntityTypesDumper(new File("output/entitytypes.java"))).run();
    }

    private File file;

    public EntityTypesDumper(File file) {
        this.file = file;
    }

    public void run() {
        StringBuilder builder = new StringBuilder();
        Set<ResourceLocation> resources = Sets.newTreeSet(Comparator.comparing(ResourceLocation::toString));
        resources.addAll(EntityType.field_200787_a.func_148742_b());
        for(ResourceLocation resourceLocation : resources) {
            String id = resourceLocation.toString();
            builder.append("public static final EntityType ")
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
