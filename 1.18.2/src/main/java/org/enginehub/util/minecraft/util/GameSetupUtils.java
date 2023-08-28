package org.enginehub.util.minecraft.util;

import com.google.common.util.concurrent.Futures;
import net.minecraft.SharedConstants;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.world.level.DataPackConfig;
import org.enginehub.util.minecraft.dumper.AbstractDumper;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class GameSetupUtils {

    public static void setupGame() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();

        AbstractDumper.OUTPUT = new File("output/" + SharedConstants.getCurrentVersion().getName());
    }

    private static final Lock lock = new ReentrantLock();
    private static ReloadableServerResources SERVER_RESOURCES;
    private static RegistryAccess SERVER_REGISTRY;

    public static ReloadableServerResources getServerResources() {
        setupGame();
        lock.lock();
        try {
            ReloadableServerResources localResources = SERVER_RESOURCES;
            if (localResources != null) {
                return localResources;
            }
            PackRepository resourcePackManager = new PackRepository(
                    PackType.SERVER_DATA,
                    new ServerPacksSource()
            );
            MinecraftServer.configurePackRepository(resourcePackManager, DataPackConfig.DEFAULT, true);
            RegistryAccess.Frozen immutable = RegistryAccess.BUILTIN.get();
            MultiPackResourceManager lifecycledResourceManager = new MultiPackResourceManager(PackType.SERVER_DATA, resourcePackManager.openAllSelected());
            CompletableFuture<ReloadableServerResources> completableFuture = (ReloadableServerResources.loadResources(lifecycledResourceManager, immutable, Commands.CommandSelection.DEDICATED, 0, ForkJoinPool.commonPool(), Runnable::run).whenComplete((dataPackContents, throwable) -> {
                if (throwable != null) {
                    lifecycledResourceManager.close();
                }
            }));
            ReloadableServerResources manager = Futures.getUnchecked(completableFuture);
            manager.updateRegistryTags(immutable);
            SERVER_RESOURCES = manager;
            return manager;
        } finally {
            lock.unlock();
        }
    }

    public static RegistryAccess getServerRegistry() {
        lock.lock();
        try {
            RegistryAccess localResources = SERVER_REGISTRY;
            if (localResources != null) {
                return localResources;
            }
            RegistryAccess manager = RegistryAccess.BUILTIN.get();
            SERVER_REGISTRY = manager;
            return manager;
        } finally {
            lock.unlock();
        }
    }
}
