package org.enginehub.util.minecraft.util;

import com.google.common.util.concurrent.Futures;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.resource.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.util.registry.DynamicRegistryManager;
import org.enginehub.util.minecraft.dumper.AbstractDumper;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();

        AbstractDumper.OUTPUT = new File("output/" + SharedConstants.getGameVersion().getName());
    }

    private static final Lock lock = new ReentrantLock();
    private static ServerResourceManager SERVER_RESOURCES;
    private static DynamicRegistryManager SERVER_REGISTRY;

    public static ServerResourceManager getServerResources() {
        setupGame();
        lock.lock();
        try {
            ServerResourceManager localResources = SERVER_RESOURCES;
            if (localResources != null) {
                return localResources;
            }
            ResourcePackManager resourcePackManager = new ResourcePackManager(
                    ResourceType.SERVER_DATA,
                    new VanillaDataPackProvider()
            );
            MinecraftServer.loadDataPacks(resourcePackManager, DataPackSettings.SAFE_MODE, true);
            DynamicRegistryManager.Impl impl = DynamicRegistryManager.create();
            CompletableFuture<ServerResourceManager> completableFuture = ServerResourceManager.reload(
                    resourcePackManager.createResourcePacks(),
                    impl,
                    CommandManager.RegistrationEnvironment.DEDICATED,
                    // permission level doesn't matter
                    0,
                    ForkJoinPool.commonPool(),
                    Runnable::run
            );
            ServerResourceManager manager = Futures.getUnchecked(completableFuture);
            manager.loadRegistryTags();
            SERVER_RESOURCES = manager;
            return manager;
        } finally {
            lock.unlock();
        }
    }

    public static DynamicRegistryManager getServerRegistry() {
        lock.lock();
        try {
            DynamicRegistryManager localResources = SERVER_REGISTRY;
            if (localResources != null) {
                return localResources;
            }
            DynamicRegistryManager manager = DynamicRegistryManager.create();
            SERVER_REGISTRY = manager;
            return manager;
        } finally {
            lock.unlock();
        }
    }
}
