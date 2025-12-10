package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class ItemTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new ItemTypesDumper().run();
    }

    public ItemTypesDumper() {
        super(
            Registries.ITEM,
            "com.sk89q.worldedit.world.item", "Item"
        );
    }

    @Override
    protected Iterator<Identifier> getDeprecatedIds() {
        return Iterators.forArray(
            Identifier.parse("cactus_green"),
            Identifier.parse("dandelion_yellow"),
            Identifier.parse("rose_red"),
            Identifier.parse("sign"),
            Identifier.parse("zombie_pigman_spawn_egg"),
            Identifier.parse("grass_path"),
            Identifier.parse("grass"),
            Identifier.parse("scute"),
            Identifier.parse("chain")
        );
    }

    @Override
    protected Iterator<Identifier> getDeprecatedTags() {
        return Iterators.forArray(
            Identifier.parse("carpets"),
            Identifier.parse("furnace_materials"),
            Identifier.parse("occludes_vibration_signals"),
            Identifier.parse("tools"),
            Identifier.parse("overworld_natural_logs"),
            Identifier.parse("axolotl_tempt_items"),
            Identifier.parse("music_discs"),
            Identifier.parse("trim_templates"),
            Identifier.parse("tall_flowers"),
            Identifier.parse("flowers"),
            Identifier.parse("enchantable/sword")
        );
    }
}
