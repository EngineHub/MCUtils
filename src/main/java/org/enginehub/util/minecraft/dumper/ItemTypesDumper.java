package org.enginehub.util.minecraft.dumper;

import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

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
}
