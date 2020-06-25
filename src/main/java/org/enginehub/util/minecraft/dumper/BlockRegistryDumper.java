package org.enginehub.util.minecraft.dumper;

import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.inventory.IClearable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.world.EmptyBlockReader;
import org.enginehub.util.minecraft.util.ReflectionUtil;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

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

        builder.registerTypeAdapter(Vector3i.class, new Vec3iAdapter());
        builder.registerTypeAdapter(Vector3d.class, new Vec3dAdapter());
    }

    @Override
    public Registry<Block> getRegistry() {
        return Registry.field_212618_g;
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return new MapComparator();
    }

    @Override
    public List<Map<String, Object>> getProperties(ResourceLocation resourceLocation, Block block) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", resourceLocation.toString());
        map.put("localizedName", LanguageMap.func_74808_a().func_230503_a_(block.func_149739_a()));
        map.put("material", getMaterial(block));
        return Lists.newArrayList(map);
    }

    private Map<String, Object> getMaterial(Block b) {
        BlockState bs = b.func_176223_P(); // getDefaultBlockState
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("powerSource", bs.func_185897_m()); // canProvidePower
        map.put("lightValue", bs.func_185906_d()); // getLightValue
        map.put("hardness", ReflectionUtil.getField(b, Block.class, "field_149782_v")); // blockHardness
        map.put("resistance", ReflectionUtil.getField(b, Block.class, "field_149781_w")); // blockResistance
        map.put("ticksRandomly", b.func_149653_t(bs)); // getTickRandomly
        VoxelShape vs = bs.func_196954_c(EmptyBlockReader.INSTANCE, BlockPos.field_177992_a);
        map.put("fullCube", !vs.func_197766_b() && isFullCube(vs.func_197752_a())); // isFullCube
        map.put("slipperiness", b.func_208618_m()); // getSlipperiness
        Material m = bs.func_185904_a(); // getMaterial
        map.put("liquid", m.func_76224_d()); // isLiquid
        map.put("solid", m.func_76220_a()); // isSolid
        map.put("movementBlocker", m.func_76230_c()); // blocksMovement
        map.put("burnable", m.func_76217_h()); // getCanBurn
        map.put("opaque", m.func_76218_k()); // isOpaque
        map.put("replacedDuringPlacement", m.func_76222_j()); // isReplaceable
        map.put("toolRequired", bs.func_235783_q_()); // isToolRequired
        map.put("fragileWhenPushed", m.func_186274_m() == PushReaction.DESTROY);
        map.put("unpushable", m.func_186274_m() == PushReaction.BLOCK);
        map.put("mapColor", rgb(m.func_151565_r().field_76291_p));
        map.put("isTranslucent", ReflectionUtil.getField(b, Block.class, "field_208621_p")); // translucent
        map.put("hasContainer", b instanceof ITileEntityProvider &&
                (((ITileEntityProvider) b).func_196283_a_(EmptyBlockReader.INSTANCE) instanceof IClearable));
        return map;
    }

    private boolean isFullCube(AxisAlignedBB aabb) {
        return aabb.field_72340_a == 0 && aabb.field_72338_b == 0 && aabb.field_72339_c == 0
                && aabb.field_72336_d == 1D && aabb.field_72337_e == 1D && aabb.field_72334_f == 1D;
    }

    public static class Vec3iAdapter extends TypeAdapter<Vector3i> {
        @Override
        public Vector3i read(final JsonReader in) throws IOException {
            throw new UnsupportedOperationException();
        }
        @Override
        public void write(final JsonWriter out, final Vector3i vec) throws IOException {
            out.beginArray();
            out.value(vec.func_177958_n()); // x
            out.value(vec.func_177956_o()); // y
            out.value(vec.func_177952_p()); // z
            out.endArray();
        }
    }

    public static class Vec3dAdapter extends TypeAdapter<Vector3d> {
        @Override
        public Vector3d read(final JsonReader in) throws IOException {
            throw new UnsupportedOperationException();
        }
        @Override
        public void write(final JsonWriter out, final Vector3d vec) throws IOException {
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