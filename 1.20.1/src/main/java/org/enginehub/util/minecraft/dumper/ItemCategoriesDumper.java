package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import com.squareup.javapoet.ClassName;
import net.minecraft.core.registries.Registries;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.getServerRegistries;
import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class ItemCategoriesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new ItemCategoriesDumper().run();
    }

    public ItemCategoriesDumper() {
        super(ClassName.get("com.sk89q.worldedit.world.item", "ItemCategory"), false);
    }

    @Override
    protected Iterator<String> getIds() {
        return getServerRegistries().registryOrThrow(Registries.ITEM).getTagNames().map(tagKey -> tagKey.location().getPath()).iterator();
    }

    @Override
    protected Iterator<String> getDeprecatedIds() {
        return Iterators.forArray(
                "carpets",
                "furnace_materials",
                "occludes_vibration_signals"
        );
    }
}
