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

public class ItemTypesDumper {

    public static void main(String[] args) {
        setupGame();
        (new ItemTypesDumper(new File("output/itemtypes.java"))).run();
    }

    private final File file;

    public ItemTypesDumper(File file) {
        this.file = file;
    }

    public void run() {
        Set<Identifier> resources = ImmutableSortedSet.copyOf(
            Comparator.comparing(Identifier::toString),
            Registry.ITEM.getIds()
        );
        StringBuilder builder = new StringBuilder();
        for(Identifier resourceLocation : resources) {
            builder.append("@Nullable public static final ItemType ")
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
