package org.enginehub.util.minecraft;

import org.enginehub.util.minecraft.dumper.Dumper;
import org.enginehub.util.minecraft.util.GameSetupUtils;

import java.util.ServiceLoader;

public class RunAll {

    public static void main(String[] args) {
        GameSetupUtils.setupGame();
        GameSetupUtils.getServerResources();
        GameSetupUtils.getServerRegistry();
        for (Dumper dumper : ServiceLoader.load(Dumper.class)) {
            System.err.println("Running dumper: " + dumper.getClass().getCanonicalName());
            dumper.run();
            System.err.println("Finished!");
        }
    }
}
