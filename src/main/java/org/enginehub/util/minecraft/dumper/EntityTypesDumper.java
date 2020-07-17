package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.ImmutableSortedSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class EntityTypesDumper {

    public static void main(String[] args) {
        setupGame();
        (new EntityTypesDumper(new File("output/entitytypes.java"))).run();
    }

    private final File file;

    public EntityTypesDumper(File file) {
        this.file = file;
    }

    public void run() {
        Set<Identifier> resources = ImmutableSortedSet.copyOf(
            Comparator.comparing(Identifier::toString),
            Registry.ENTITY_TYPE.getIds()
        );
        StringBuilder builder = new StringBuilder();
        for (Identifier resourceLocation : resources) {
            builder.append("@Nullable public static final EntityType ")
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
