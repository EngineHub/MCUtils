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
    protected Collection<Identifier> getIds() {
        return Registry.ITEM.getIds();
    }

    @Override
    protected Collection<Identifier> getDeprecatedIds() {
        return ImmutableSet.of(
                new Identifier("cactus_green"),
                new Identifier("dandelion_yellow"),
                new Identifier("rose_red"),
                new Identifier("sign"),
                new Identifier("zombie_pigman_spawn_egg"),
                new Identifier("grass_path")
        );
    }
}
