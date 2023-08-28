package org.enginehub.util.minecraft.util;

import com.google.common.util.concurrent.Futures;
import net.minecraft.SharedConstants;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
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
    private static ServerResources SERVER_RESOURCES;
    private static RegistryAccess SERVER_REGISTRY;

    public static ServerResources getServerResources() {
        setupGame();
        lock.lock();
        try {
            ServerResources localResources = SERVER_RESOURCES;
            if (localResources != null) {
                return localResources;
            }
            PackRepository resourcePackManager = new PackRepository(
                    PackType.SERVER_DATA,
                    new ServerPacksSource()
            );
            MinecraftServer.configurePackRepository(resourcePackManager, DataPackConfig.DEFAULT, true);
            RegistryAccess.RegistryHolder impl = RegistryAccess.builtin();
            CompletableFuture<ServerResources> completableFuture = ServerResources.loadResources(
                    resourcePackManager.openAllSelected(),
                    impl,
                    Commands.CommandSelection.DEDICATED,
                    // permission level doesn't matter
                    0,
                    ForkJoinPool.commonPool(),
                    Runnable::run
            );
            ServerResources manager = Futures.getUnchecked(completableFuture);
            manager.updateGlobals();
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
            RegistryAccess manager = RegistryAccess.builtin();
            SERVER_REGISTRY = manager;
            return manager;
        } finally {
            lock.unlock();
        }
    }
}
