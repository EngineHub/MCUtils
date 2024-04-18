package org.enginehub.util.minecraft.dumper;

import com.google.auto.service.AutoService;
import com.google.common.collect.Lists;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Clearable;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.enginehub.util.minecraft.util.GameSetupUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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
        builder.registerTypeAdapter(Vec3.class, new Vec3dAdapter());
    }

    @Override
    public Registry<Block> getRegistry() {
        return GameSetupUtils.getServerRegistries().registryOrThrow(Registries.BLOCK);
    }

    @Override
    public Comparator<Map<String, Object>> getComparator() {
        return Comparator.comparing(map -> (String) map.get("id"));
    }

    @Override
    public List<Map<String, Object>> getProperties(ResourceLocation resourceLocation, Block block) {
        Map<String, Object> map = new TreeMap<>();
        map.put("id", resourceLocation.toString());
        map.put("localizedName", Language.getInstance().getOrDefault(block.getDescriptionId()));
        map.put("material", getMaterial(block));
        return Lists.newArrayList(map);
    }

    private Map<String, Object> getMaterial(Block b) {
        BlockState bs = b.defaultBlockState();
        Field mapColorField;
        try {
            mapColorField = BlockBehaviour.BlockStateBase.class.getDeclaredField("mapColor");
            mapColorField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        Map<String, Object> map = new TreeMap<>();



        map.put("powerSource", bs.isSignalSource());
        map.put("lightValue", bs.getLightEmission());
        map.put("hardness", bs.getDestroySpeed(null, null));
        map.put("resistance", b.getExplosionResistance());
        map.put("ticksRandomly", bs.isRandomlyTicking());
        VoxelShape vs = bs.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
        map.put("fullCube", !vs.isEmpty() && isFullCube(vs.bounds()));
        map.put("slipperiness", b.getFriction());
        map.put("liquid", bs.liquid());
        map.put("solid", bs.isSolid());
        map.put("movementBlocker", bs.blocksMotion());
        map.put("burnable", bs.ignitedByLava());
        map.put("opaque", bs.canOcclude());
        map.put("replacedDuringPlacement", bs.canBeReplaced());
        map.put("toolRequired", bs.requiresCorrectToolForDrops());
        map.put("fragileWhenPushed", bs.getPistonPushReaction() == PushReaction.DESTROY);
        map.put("unpushable", bs.getPistonPushReaction() == PushReaction.BLOCK);
        try {
            map.put("mapColor", rgb(((MapColor)mapColorField.get(bs)).col));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        map.put("hasContainer", b instanceof EntityBlock &&
            (((EntityBlock) b).newBlockEntity(BlockPos.ZERO, bs) instanceof Clearable));
        return map;
    }

    private static final AABB FULL_CUBE = AABB.unitCubeFromLowerCorner(Vec3.ZERO);

    private boolean isFullCube(AABB aabb) {
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

    public static class Vec3dAdapter extends TypeAdapter<Vec3> {
        @Override
        public Vec3 read(final JsonReader in) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(final JsonWriter out, final Vec3 vec) throws IOException {
            out.beginArray();
            out.value(vec.x());
            out.value(vec.y());
            out.value(vec.z());
            out.endArray();
        }
    }
}
