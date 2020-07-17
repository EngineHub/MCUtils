package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.ImmutableSortedSet;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Set;

public abstract class RegistryClassDumper {

    private final String type;
    private final File file;

    protected RegistryClassDumper(String type, File file) {
        this.type = type;
        this.file = file;
    }

    protected abstract Collection<Identifier> getIds();

    public void run() {
        Set<Identifier> resources = ImmutableSortedSet.copyOf(
            Comparator.comparing(Identifier::toString),
            getIds()
        );
        StringBuilder builder = new StringBuilder();
        for (Identifier resourceLocation : resources) {
            builder.append("@Nullable public static final ").append(type).append(" ")
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
