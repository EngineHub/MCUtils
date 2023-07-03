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
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.EmptyBlockView;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.enginehub.util.minecraft.util.GameSetupUtils.setupGame;

public class BlockRegistryDumper extends RegistryDumper<Block> {

    private static final Box FULL_CUBE = Box.from(MutableIntBoundingBox.empty());

    public BlockRegistryDumper(File file) {
        super(file);
    }

    public static void main(String[] args) {
        setupGame();
        new Default().run();
    }

    @Override
    public void registerAdapters(GsonBuilder builder) {
        super.registerAdapters(builder);

        builder.registerTypeAdapter(Vec3i.class, new Vec3iAdapter());
        builder.registerTypeAdapter(Vec3d.class, new Vec3dAdapter());
    }

    @Override
    public Collection<String> getIds() {
        return Registry.BLOCK.getIds().stream().map(Identifier::toString).toList();
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return Comparator.comparing(map -> (String) map.get("id"));
    }

    @Override
    public List<Map<String, Object>> getProperties(String resourceLocation) {
        Block block = Registry.BLOCK.get(new Identifier(resourceLocation));
        Map<String, Object> map = new TreeMap<>();
        map.put("id", resourceLocation);
        map.put("localizedName", Language.getInstance().translate(block.getTranslationKey()));
        map.put("material", getMaterial(block));
        return Lists.newArrayList(map);
    }

    private Map<String, Object> getMaterial(Block b) {
        BlockState bs = b.getDefaultState();
        Map<String, Object> map = new TreeMap<>();
        map.put("powerSource", bs.emitsRedstonePower());
        map.put("lightValue", bs.getLuminance());
        map.put("hardness", bs.getHardness(EmptyBlockView.INSTANCE, BlockPos.ORIGIN));
        map.put("resistance", b.getBlastResistance());
        map.put("ticksRandomly", b.hasRandomTicks(bs));
        VoxelShape vs = bs.getCollisionShape(EmptyBlockView.INSTANCE, BlockPos.ORIGIN);
        map.put("fullCube", !vs.isEmpty() && isFullCube(vs.getBoundingBox()));
        map.put("slipperiness", b.getSlipperiness());
        map.put("translucent", bs.isTranslucent(EmptyBlockView.INSTANCE, BlockPos.ORIGIN));
        Material m = bs.getMaterial();
        map.put("liquid", m.isLiquid());
        map.put("solid", m.isSolid());
        map.put("movementBlocker", m.blocksMovement());
        map.put("burnable", m.isBurnable());
        map.put("opaque", m.blocksLight());
        map.put("replacedDuringPlacement", m.isReplaceable());
        map.put("toolRequired", !m.canBreakByHand());
        map.put("fragileWhenPushed", m.getPistonBehavior() == PistonBehavior.DESTROY);
        map.put("unpushable", m.getPistonBehavior() == PistonBehavior.BLOCK);
        map.put("mapColor", rgb(m.getColor().color));
        map.put("hasContainer", b instanceof BlockEntityProvider bep &&
                bep.createBlockEntity(EmptyBlockView.INSTANCE) instanceof Clearable);
        return map;
    }

    private boolean isFullCube(Box aabb) {
        return aabb.equals(FULL_CUBE);
    }

    @AutoService(Dumper.class)
    public static class Default implements Dumper {
        @Override
        public void run() {
            new BlockRegistryDumper(new File(OUTPUT, "blocks.json")).run();
        }
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
