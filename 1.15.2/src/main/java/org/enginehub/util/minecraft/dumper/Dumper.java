package org.enginehub.util.minecraft.dumper;

import java.io.File;

public interface Dumper {

    File OUTPUT = new File("output/1.15.2");

    void run();
}
