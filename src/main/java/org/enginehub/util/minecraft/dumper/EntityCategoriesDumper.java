package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.Sets;
import net.minecraft.data.EntityTypeTagsProvider;
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

public class EntityCategoriesDumper {

    // worldedit doesn't actually do entity type categories yet
    public static void main(String[] args) {
        setupGame();
        (new EntityCategoriesDumper(new File("output/entitycategories.java"))).run();
    }

    private File file;

    public EntityCategoriesDumper(File file) {
        this.file = file;
    }

    @SuppressWarnings("unchecked")
    private void run() {
        StringBuilder builder = new StringBuilder();
        Set<ResourceLocation> resources = Sets.newTreeSet(Comparator.comparing(ResourceLocation::toString));
        final EntityTypeTagsProvider provider = new EntityTypeTagsProvider(null);
        ReflectionUtil.invokeMethod(provider, EntityTypeTagsProvider.class, "func_200432_c", null, null); // initialize

        Map<Object, Object> map = (Map<Object, Object>) ReflectionUtil.getField(provider, TagsProvider.class, "field_200434_b");
        for (Object o : map.keySet()) {
            resources.add(((Tag<?>) o).func_199886_b());
        }

        for(ResourceLocation resourceLocation : resources) {
            String id = resourceLocation.toString();
            builder.append("@Nullable public static final EntityTypeCategory ")
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

