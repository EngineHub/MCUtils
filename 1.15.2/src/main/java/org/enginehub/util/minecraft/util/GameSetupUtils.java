package org.enginehub.util.minecraft.util;

import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import org.enginehub.util.minecraft.dumper.AbstractDumper;

import java.io.File;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.getCurrentVersion();
        Bootstrap.bootStrap();

        AbstractDumper.OUTPUT = new File("output/" + SharedConstants.getCurrentVersion().getName());
    }
}
