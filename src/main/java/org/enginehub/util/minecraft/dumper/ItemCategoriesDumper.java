package org.enginehub.util.minecraft.dumper;

import com.squareup.javapoet.ClassName;
import net.minecraft.util.Identifier;

import java.util.Collection;

import static org.enginehub.util.minecraft.util.GameSetupUtils.loadServerResources;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class ItemCategoriesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new ItemCategoriesDumper().run();
    }

    public ItemCategoriesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.item", "ItemCategory"), false);
    }

    @Override
    protected Collection<Identifier> getIds() {
        return loadServerResources().getRegistryTagManager().items().getKeys();
    }
}

