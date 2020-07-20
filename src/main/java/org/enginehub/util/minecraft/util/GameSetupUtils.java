package org.enginehub.util.minecraft.util;

import com.google.common.util.concurrent.Futures;
import net.minecraft.Bootstrap;
import net.minecraft.resource.DataPackSettings;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.resource.VanillaDataPackProvider;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class GameSetupUtils {

    public static void setupGame() {
        Bootstrap.initialize();
    }

    private static final Lock lock = new ReentrantLock();
    private static ServerResourceManager SERVER_RESOURCES;

    public static ServerResourceManager getServerResources() {
        setupGame();
        lock.lock();
        try {
            ServerResourceManager localResources = SERVER_RESOURCES;
            if (localResources != null) {
                return localResources;
            }
            ResourcePackManager<ResourcePackProfile> resourcePackManager = new ResourcePackManager<>(
                ResourcePackProfile::new,
                new VanillaDataPackProvider()
            );
            MinecraftServer.loadDataPacks(resourcePackManager, DataPackSettings.SAFE_MODE, true);
            CompletableFuture<ServerResourceManager> completableFuture = ServerResourceManager.reload(
                resourcePackManager.createResourcePacks(),
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
}
