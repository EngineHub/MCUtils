package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.util.Clearable;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EmptyBlockView;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BlockRegistryDumper extends RegistryDumper<Block> {

    public static void main(String[] args) {
        setupGame();
        new Default().run();
    }

    @AutoService(Dumper.class)
    public static class Default implements Dumper {
        @Override
        public void run() {
            new BlockRegistryDumper(new File("output/blocks.json")).run();
        }
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
    public Registry<Block> getRegistry() {
        return Registry.BLOCK;
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return Comparator.comparing(map -> (String) map.get("id"));
    }

    @Override
    public List<Map<String, Object>> getProperties(Identifier resourceLocation, Block block) {
        Map<String, Object> map = new TreeMap<>();
        map.put("id", resourceLocation.toString());
        map.put("localizedName", Language.getInstance().get(block.getTranslationKey()));
        map.put("material", getMaterial(block));
        return Lists.newArrayList(map);
    }

    private Map<String, Object> getMaterial(Block b) {
        BlockState bs = b.getDefaultState();
        Map<String, Object> map = new TreeMap<>();
        map.put("powerSource", bs.emitsRedstonePower());
        map.put("lightValue", bs.getLuminance());
        map.put("hardness", bs.getHardness(null, null));
        map.put("resistance", b.getBlastResistance());
        map.put("ticksRandomly", b.hasRandomTicks(bs));
        VoxelShape vs = bs.getCollisionShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
        map.put("fullCube", !vs.isEmpty() && isFullCube(vs.getBoundingBox()));
        map.put("slipperiness", b.getSlipperiness());
        Material m = bs.getMaterial();
        map.put("liquid", m.isLiquid());
        map.put("solid", m.isSolid());
        map.put("movementBlocker", m.blocksMovement());
        map.put("burnable", m.isBurnable());
        map.put("opaque", m.blocksLight());
        map.put("replacedDuringPlacement", m.isReplaceable());
        map.put("toolRequired", bs.isToolRequired());
        map.put("fragileWhenPushed", m.getPistonBehavior() == PistonBehavior.DESTROY);
        map.put("unpushable", m.getPistonBehavior() == PistonBehavior.BLOCK);
        map.put("mapColor", rgb(m.getColor().color));
        map.put("hasContainer", b instanceof BlockEntityProvider &&
            (((BlockEntityProvider) b).createBlockEntity(BlockPos.ORIGIN, bs) instanceof Clearable));
        return map;
    }

    private static final Box FULL_CUBE = Box.from(Vec3d.ZERO);

    private boolean isFullCube(Box aabb) {
        return aabb.equals(FULL_CUBE);
    }

    public static class Vec3iAdapter extends TypeAdapter<Vec3i> {
        @Override
        public Vec3i read(final JsonReader in) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void write(final JsonWriter out, final Vec3i vec) throws IOException {
            out.beginArray();
            out.value(vec.getX());
            out.value(vec.getY());
            out.value(vec.getZ());
            out.endArray();
        }
    }

    public static class Vec3dAdapter extends TypeAdapter<Vec3d> {
        @Override
        public Vec3d read(final JsonReader in) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void write(final JsonWriter out, final Vec3d vec) throws IOException {
            out.beginArray();
            out.value(vec.getX());
            out.value(vec.getY());
            out.value(vec.getZ());
            out.endArray();
        }
    }
}
