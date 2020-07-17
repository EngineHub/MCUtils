package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.ImmutableSortedSet;
import net.minecraft.item.Item;
import net.minecraft.tag.RegistryTagContainer;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Set;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class ItemCategoriesDumper {

    public static void main(String[] args) {
        setupGame();
        (new ItemCategoriesDumper(new File("output/itemcategories.java"))).run();
    }

    private final File file;

    public ItemCategoriesDumper(File file) {
        this.file = file;
    }

    private void run() {
        RegistryTagContainer<Item> itemTags = loadServerResources().getRegistryTagManager().items();
        Set<Identifier> resources = ImmutableSortedSet.copyOf(
            Comparator.comparing(Identifier::toString),
            itemTags.getKeys()
        );

        StringBuilder builder = new StringBuilder();
        for(Identifier resourceLocation : resources) {
            builder.append("public static final ItemCategory ")
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

