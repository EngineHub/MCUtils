package org.enginehub.util.minecraft.dumper;

import java.io.File;

public interface Dumper {

    File OUTPUT = new File("output/1.16.5");

    void run();
}
