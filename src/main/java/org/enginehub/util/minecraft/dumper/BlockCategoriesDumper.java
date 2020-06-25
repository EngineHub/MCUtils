package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.Sets;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.TagsProvider;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import org.enginehub.util.minecraft.util.ReflectionUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BlockCategoriesDumper {

    public static void main(String[] args) {
        setupGame();
        (new BlockCategoriesDumper(new File("output/blockcategories.java"))).run();
    }

    private File file;

    public BlockCategoriesDumper(File file) {
        this.file = file;
    }

    @SuppressWarnings("unchecked")
    private void run() {
        StringBuilder builder = new StringBuilder();
        Set<ResourceLocation> resources = Sets.newTreeSet(Comparator.comparing(ResourceLocation::toString));
        final BlockTagsProvider provider = new BlockTagsProvider(null);
        ReflectionUtil.invokeMethod(provider, BlockTagsProvider.class, "func_200432_c", null, null); // initialize

        Map<ResourceLocation, net.minecraft.tags.ITag.Builder> map = (Map<ResourceLocation, net.minecraft.tags.ITag.Builder>) ReflectionUtil.getField(provider, TagsProvider.class, "field_200434_b");
        resources.addAll(map.keySet());

        for(ResourceLocation resourceLocation : resources) {
            String id = resourceLocation.toString();
            builder.append("public static final BlockCategory ")
                    .append(id.split(":")[1].toUpperCase())
                    .append(" = get(\"")
                    .append(id)
                    .append("\");\n");
        }
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

