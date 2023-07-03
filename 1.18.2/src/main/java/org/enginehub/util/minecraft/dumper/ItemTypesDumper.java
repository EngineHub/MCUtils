package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class ItemTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new ItemTypesDumper().run();
    }

    public ItemTypesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.item", "ItemType"), true);
    }

    @Override
    protected Collection<String> getIds() {
        return Registry.ITEM.getIds().stream().map(Identifier::getPath).toList();
    }

    @Override
    protected Collection<String> getDeprecatedIds() {
        return ImmutableSet.of(
            "cactus_green",
            "dandelion_yellow",
            "rose_red",
            "sign",
            "zombie_pigman_spawn_egg",
            "grass_path"
        );
    }
}
