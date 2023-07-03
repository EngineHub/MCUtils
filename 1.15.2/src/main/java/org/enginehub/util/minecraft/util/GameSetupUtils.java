package org.enginehub.util.minecraft.util;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import org.enginehub.util.minecraft.dumper.AbstractDumper;

import java.io.File;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.getGameVersion();
        Bootstrap.initialize();

        AbstractDumper.OUTPUT = new File("output/" + SharedConstants.getGameVersion().getName());
    }
}
