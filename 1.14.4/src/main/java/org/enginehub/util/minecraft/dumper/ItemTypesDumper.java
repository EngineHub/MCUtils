package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class ItemTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new ItemTypesDumper().run();
    }

    public ItemTypesDumper() {
        super("com.sk89q.worldedit.world.item", "Item");
    }

    @Override
    protected Iterator<String> getIds() {
        return Registry.ITEM.keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getTags() {
        return ItemTags.getAllTags().getAvailableTags().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "cactus_green",
                "dandelion_yellow",
                "rose_red",
                "sign"
        );
    }
}
