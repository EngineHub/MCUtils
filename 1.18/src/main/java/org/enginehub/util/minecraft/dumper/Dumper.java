package org.enginehub.util.minecraft.dumper;

import net.minecraft.SharedConstants;

import java.io.File;

public interface Dumper {

    File OUTPUT = new File("output/" + SharedConstants.getGameVersion().getName());

    void run();
}
