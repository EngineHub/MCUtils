package org.enginehub.util.minecraft.util;

import com.mojang.serialization.Lifecycle;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.*;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.enginehub.util.minecraft.dumper.AbstractDumper;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        AbstractDumper.OUTPUT = new File("output/" + SharedConstants.getCurrentVersion().getName());
    }

    private static final Lock lock = new ReentrantLock();
    private static final GameRules GAME_RULES = Util.make(new GameRules(), gameRules -> {
        gameRules.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
        gameRules.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
    });
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
                    new ServerPacksSource()
            );
            WorldDataConfiguration wdc = new WorldDataConfiguration(DataPackConfig.DEFAULT, FeatureFlags.DEFAULT_FLAGS);
            WorldLoader.PackConfig dataPacks = new WorldLoader.PackConfig(resourcePackManager, wdc, false, true);
            WorldLoader.InitConfig serverConfig = new WorldLoader.InitConfig(dataPacks, Commands.CommandSelection.DEDICATED, 4);

            WorldStem saveLoader = Util.blockUntilDone(executor -> WorldLoader.load(serverConfig, (dataLoadContext) -> {
                LevelSettings dataGenLevel = new LevelSettings("Data Gen Level", GameType.CREATIVE, false, Difficulty.NORMAL, true, GAME_RULES, dataLoadContext.dataConfiguration());
                RegistryAccess.Frozen immutable = dataLoadContext.datapackDimensions().freeze();

                PrimaryLevelData saveProperties = new PrimaryLevelData(dataGenLevel, WorldOptions.DEMO_OPTIONS, PrimaryLevelData.SpecialWorldProperty.FLAT, Lifecycle.stable());
                return new WorldLoader.DataLoadOutput<>(saveProperties, immutable);
            }, WorldStem::new, Util.backgroundExecutor(), executor)).get();
            return SERVER_REGISTRY_MANAGER = saveLoader.registries().compositeAccess();
        } catch (ExecutionException e) {
            throw new RuntimeException(e.getCause());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
