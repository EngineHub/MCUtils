package org.enginehub.util.minecraft.dumper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AbstractDumper implements Dumper {
    /**
     * The output directory for the files. Is set in each individual module to "output/(Minecraft version)".
     */
    public static File OUTPUT = null;

    protected AbstractDumper() {
        Path outputPath = OUTPUT.toPath();
        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
