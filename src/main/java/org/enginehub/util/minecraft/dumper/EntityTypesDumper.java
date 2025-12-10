package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class EntityTypesDumper extends RegistryClassDumper {

    public static void main(String[] args) {
        setupGame();
        new EntityTypesDumper().run();
    }

    public EntityTypesDumper() {
        super(
            Registries.ENTITY_TYPE,
            "com.sk89q.worldedit.world.entity", "Entity"
        );
    }

    @Override
    protected Iterator<Identifier> getDeprecatedIds() {
        return Iterators.forArray(
            Identifier.parse("zombie_pigman"),
            Identifier.parse("boat"),
            Identifier.parse("chest_boat"),
            Identifier.parse("creaking_transient"),
            Identifier.parse("potion")
        );
    }
}
