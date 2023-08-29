package org.enginehub.util.minecraft.util;

import net.minecraft.SharedConstants;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.*;
import org.enginehub.util.minecraft.dumper.AbstractDumper;

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.getCurrentVersion();
        Bootstrap.bootStrap();

        AbstractDumper.OUTPUT = new File("output/" + SharedConstants.getCurrentVersion().getName());

        BlockTags.reset(setupTags(new BlockTagsProvider(null)));
        ItemTags.reset(setupTags(new ItemTagsProvider(null)));
        EntityTypeTags.reset(setupTags(new EntityTypeTagsProvider(null)));
    }

    private static <T> TagCollection<T> setupTags(TagsProvider<T> tagsProvider) {
        Map<Tag<T>, Tag.Builder<T>> builders = (Map<Tag<T>, Tag.Builder<T>>) ReflectionUtil.getField(tagsProvider, TagsProvider.class, "builders");
        builders.clear();
        ReflectionUtil.invokeMethod(tagsProvider, tagsProvider.getClass(), "addTags", null, null);
        TagCollection<T> tagCollection = new TagCollection<>(resourceLocation -> Optional.empty(), "", false, "generated");
        Map<ResourceLocation, Tag.Builder<T>> map = builders.entrySet().stream().collect(Collectors.toMap(entry -> entry.getKey().getId(), Map.Entry::getValue));
        tagCollection.load(map);
        return tagCollection;
    }
}
