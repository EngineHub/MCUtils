package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistries;
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
        return getServerRegistries().registryOrThrow(Registries.ITEM).keySet().stream().map(ResourceLocation::getPath).iterator();
    }

    @Override
    protected Iterator<String> getTags() {
        return getServerRegistries().registryOrThrow(Registries.ITEM).getTagNames().map(tagKey -> tagKey.location().getPath()).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "cactus_green",
                "dandelion_yellow",
                "rose_red",
                "sign",
                "zombie_pigman_spawn_egg",
                "grass_path"
        );
    }

    @Override
    protected Iterator<String> getDeprecatedTags() {
        return Iterators.forArray(
                "carpets",
                "furnace_materials",
                "occludes_vibration_signals"
        );
    }
}
