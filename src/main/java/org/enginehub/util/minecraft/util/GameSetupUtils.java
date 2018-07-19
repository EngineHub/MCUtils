package org.enginehub.util.minecraft.util;

import net.minecraft.block.Block;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.Item;

public class GameSetupUtils {

    public static void setupGame() {
        Bootstrap.func_151354_b();
        Block.func_149671_p();
        Item.func_150900_l();
    }
}
