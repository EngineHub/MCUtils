package org.enginehub.util.minecraft.dumper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

abstract class RegistryDumper<V> {

    private File file;
    private Gson gson;

    public RegistryDumper(File file) {
        this.file = file;
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        registerAdapters(builder);
        this.gson = builder.create();
    }

    public void registerAdapters(GsonBuilder builder) {

    }

    public void run() {
        List<Map<String, Object>> list = new LinkedList<>();

        RegistryNamespaced<ResourceLocation, V> registry = getRegistry();
        for (ResourceLocation resourceLocation : registry.func_148742_b()) {
            list.addAll(getProperties(resourceLocation, registry.func_82594_a(resourceLocation)));
        }

        list.sort(getComparator());
        String out = gson.toJson(list);
        this.write(out);
        System.out.println("Wrote file: " + file.getAbsolutePath());
    }

    private void write(String s) {
        try(FileOutputStream str = new FileOutputStream(file)) {
            str.write(s.getBytes());
        } catch (IOException e) {
            System.err.println("Error writing registry dump: ");
            e.printStackTrace();
        }
    }

    protected String rgb(int i) {
        int r = (i >> 16) & 0xFF;
        int g = (i >>  8) & 0xFF;
        int b = i & 0xFF;
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public abstract List<Map<String, Object>> getProperties(ResourceLocation resourceLocation, V object);

    public abstract RegistryNamespaced<ResourceLocation, V> getRegistry();

    public abstract Comparator<Map<String, Object>> getComparator();
}

