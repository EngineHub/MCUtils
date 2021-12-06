package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class RegistryDumper<V> implements Dumper {

    private final File file;
    private final Gson gson;

    protected RegistryDumper(File file) {
        this.file = file;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        registerAdapters(builder);
        gson = builder.create();
    }

    public void registerAdapters(GsonBuilder builder) {

    }

    @Override
    public void run() {
        Registry<V> registry = getRegistry();
        ImmutableList<Map<String, Object>> list = ImmutableList.sortedCopyOf(
                getComparator(),
                getRegistry().getIds().stream()
                        .flatMap(v -> getProperties(v, registry.get(v)).stream())
                        .collect(Collectors.toList())
        );

        String out = gson.toJson(list);
        write(out);
        System.out.println("Wrote file: " + file.getAbsolutePath());
    }

    private void write(String s) {
        try (FileOutputStream str = new FileOutputStream(file)) {
            str.write(s.getBytes());
        } catch (IOException e) {
            System.err.println("Error writing registry dump: ");
            e.printStackTrace();
        }
    }

    protected String rgb(int i) {
        int r = i >> 16 & 0xFF;
        int g = i >> 8 & 0xFF;
        int b = i & 0xFF;
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public abstract List<Map<String, Object>> getProperties(Identifier resourceLocation, V object);

    public abstract Registry<V> getRegistry();

    public abstract Comparator<Map<String, Object>> getComparator();
}
