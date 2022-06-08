package org.enginehub.util.minecraft.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Lifecycle;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.PrimaryLevelData;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    private static final Lock lock = new ReentrantLock();
    private static final GameRules GAME_RULES = Util.make(new GameRules(), gameRules -> {
        gameRules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
        gameRules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
    });
    private static final LevelSettings DATA_GEN_LEVEL = new LevelSettings("Data Gen Level", GameType.CREATIVE, false, Difficulty.NORMAL, true, GAME_RULES, DataPackConfig.DEFAULT);
    private static RegistryAccess SERVER_REGISTRY_MANAGER;

    public static RegistryAccess getServerRegistries() {
        setupGame();
        lock.lock();
        try {
            RegistryAccess localResources = SERVER_REGISTRY_MANAGER;
            if (localResources != null) {
                return localResources;
            }
            PackRepository resourcePackManager = new PackRepository(
                PackType.SERVER_DATA,
                new ServerPacksSource()
            );
            WorldLoader.PackConfig dataPacks = new WorldLoader.PackConfig(resourcePackManager, DataPackConfig.DEFAULT, false);
            WorldLoader.InitConfig serverConfig = new WorldLoader.InitConfig(dataPacks, Commands.CommandSelection.DEDICATED, 4);

            WorldStem saveLoader = Util.blockUntilDone(executor -> WorldStem.load(serverConfig, (resourceManager, dataPackSettings) -> {
                RegistryAccess.Frozen immutable = RegistryAccess.BUILTIN.get();
                WorldGenSettings generatorOptions = immutable.registryOrThrow(Registry.WORLD_PRESET_REGISTRY).getHolderOrThrow(WorldPresets.FLAT).value().createWorldGenSettings(0L, false, false);
                PrimaryLevelData saveProperties = new PrimaryLevelData(DATA_GEN_LEVEL, generatorOptions, Lifecycle.stable());
                return Pair.of(saveProperties, immutable);
            }, Util.backgroundExecutor(), executor)).get();
            return SERVER_REGISTRY_MANAGER = saveLoader.registryAccess();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
