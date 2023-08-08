package org.enginehub.util.minecraft.util;

import com.google.common.util.concurrent.Futures;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import net.minecraft.resource.*;
import net.minecraft.server.DataPackContents;
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
        // FIXME Bootstrap can not properly initialize for 1.18.2 because calling it leads to an IllegalAccessError later down the call stack
        Bootstrap.initialize();

        AbstractDumper.OUTPUT = new File("output/" + SharedConstants.getGameVersion().getName());
    }

    private static final Lock lock = new ReentrantLock();
    private static DataPackContents SERVER_RESOURCES;
    private static DynamicRegistryManager SERVER_REGISTRY;

    public static DataPackContents getServerResources() {
        setupGame();
        lock.lock();
        try {
            DataPackContents localResources = SERVER_RESOURCES;
            if (localResources != null) {
                return localResources;
            }
            ResourcePackManager resourcePackManager = new ResourcePackManager(
                    ResourceType.SERVER_DATA,
                    new VanillaDataPackProvider()
            );
            MinecraftServer.loadDataPacks(resourcePackManager, DataPackSettings.SAFE_MODE, true);
            DynamicRegistryManager.Immutable immutable = DynamicRegistryManager.BUILTIN.get();
            LifecycledResourceManagerImpl lifecycledResourceManager = new LifecycledResourceManagerImpl(ResourceType.SERVER_DATA, resourcePackManager.createResourcePacks());
            CompletableFuture<DataPackContents> completableFuture = (DataPackContents.reload(lifecycledResourceManager, immutable, CommandManager.RegistrationEnvironment.DEDICATED, 0, ForkJoinPool.commonPool(), Runnable::run).whenComplete((dataPackContents, throwable) -> {
                if (throwable != null) {
                    lifecycledResourceManager.close();
                }
            }));
            DataPackContents manager = Futures.getUnchecked(completableFuture);
            manager.refresh(immutable);
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
            DynamicRegistryManager manager = DynamicRegistryManager.BUILTIN.get();
            SERVER_REGISTRY = manager;
            return manager;
        } finally {
            lock.unlock();
        }
    }
}
