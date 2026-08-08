package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Iterators;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

import java.util.Iterator;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

@AutoService(Dumper.class)
public class FluidTypesDumper extends RegistryClassDumper {

    static void main(String[] args) {
        setupGame();
        new FluidTypesDumper().run();
    }

    public FluidTypesDumper() {
        super(
            Registries.FLUID,
            "com.sk89q.worldedit.world.fluid", "Fluid"
        );
    }

    @Override
    protected Iterator<Identifier> getDeprecatedIds() {
        return Iterators.forArray();
    }

    @Override
    protected Iterator<Identifier> getDeprecatedTags() {
        return Iterators.forArray();
    }
}
