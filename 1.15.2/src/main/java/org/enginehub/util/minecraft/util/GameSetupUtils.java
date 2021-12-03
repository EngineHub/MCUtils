package org.enginehub.util.minecraft.util;

import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.getGameVersion();
        Bootstrap.initialize();
    }
}
