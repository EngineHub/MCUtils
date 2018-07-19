package org.enginehub.util.minecraft.dumper;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.RegistryNamespacedDefaultedByKey;
import net.minecraft.util.text.translation.LanguageMap;
import org.enginehub.util.minecraft.util.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlockRegistryDumper extends RegistryDumper<Block> {

    public static void main(String[] args) {
        setupGame();
        (new BlockRegistryDumper(new File("output/blocks.json"))).run();
    }

    public BlockRegistryDumper(File file) {
        super(file);
    }

    @Override
    public void registerAdapters(GsonBuilder builder) {
        super.registerAdapters(builder);

        builder.registerTypeAdapter(Vec3i.class, new Vec3iAdapter());
        builder.registerTypeAdapter(Vec3d.class, new Vec3dAdapter());
    }

    @Override
    public RegistryNamespacedDefaultedByKey<ResourceLocation, Block> getRegistry() {
        return Block.field_149771_c;
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return new MapComparator();
    }

    @Override
    public List<Map<String, Object>> getProperties(ResourceLocation resourceLocation, Block block) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", resourceLocation.toString());
        map.put("localizedName", LanguageMap.func_74808_a().func_74805_b(block.func_149739_a()));
        map.put("material", getMaterial(block));
        return Lists.newArrayList(map);
    }

    private Map<String, Object> getMaterial(Block b) {
        IBlockState bs = b.func_176223_P(); // getDefaultBlockState
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("powerSource", bs.func_185897_m()); // canProvidePower
        map.put("lightValue", bs.func_185906_d()); // getLightValue
        map.put("hardness", ReflectionUtil.getField(b, Block.class, "blockHardness", "field_149782_v"));
        map.put("resistance", ReflectionUtil.getField(b, Block.class, "blockResistance", "field_149781_w"));
        map.put("ticksRandomly", b.func_149653_t(bs)); // getTickRandomly
        map.put("fullCube", bs.func_185917_h()); // isFullCube
        map.put("slipperiness", b.func_208618_m()); // getSlipperiness
        Material m = bs.func_185904_a(); // getMaterial
        map.put("liquid", m.func_76224_d()); // isLiquid
        map.put("solid", m.func_76220_a()); // isSolid
        map.put("movementBlocker", m.func_76230_c()); // blocksMovement
        map.put("burnable", m.func_76217_h()); // getCanBurn
        map.put("opaque", m.func_76218_k()); // isOpaque
        map.put("replacedDuringPlacement", m.func_76222_j()); // isReplaceable
        map.put("toolRequired", !m.func_76229_l()); // isToolNotRequired
        map.put("fragileWhenPushed", m.func_186274_m() == EnumPushReaction.DESTROY);
        map.put("unpushable", m.func_186274_m() == EnumPushReaction.BLOCK);
        map.put("mapColor", rgb(m.func_151565_r().field_76291_p));
        map.put("isTranslucent", ReflectionUtil.getField(b, Block.class, "translucent", "field_208621_p"));
        map.put("hasContainer", b instanceof BlockContainer);
        return map;
    }

    public static class Vec3iAdapter extends TypeAdapter<Vec3i> {
        @Override
        public Vec3i read(final JsonReader in) throws IOException {
            throw new UnsupportedOperationException();
        }
        @Override
        public void write(final JsonWriter out, final Vec3i vec) throws IOException {
            out.beginArray();
            out.value(vec.func_177958_n()); // x
            out.value(vec.func_177956_o()); // y
            out.value(vec.func_177952_p()); // z
            out.endArray();
        }
    }

    public static class Vec3dAdapter extends TypeAdapter<Vec3d> {
        @Override
        public Vec3d read(final JsonReader in) throws IOException {
            throw new UnsupportedOperationException();
        }
        @Override
        public void write(final JsonWriter out, final Vec3d vec) throws IOException {
            out.beginArray();
            out.value(vec.field_72450_a);
            out.value(vec.field_72448_b);
            out.value(vec.field_72449_c);
            out.endArray();
        }
    }

    private static class MapComparator implements Comparator<Map<String, Object>> {
        @Override
        public int compare(Map<String, Object> a, Map<String, Object> b) {
            return ((String) a.get("id")).compareTo((String) b.get("id"));
        }
    }
}