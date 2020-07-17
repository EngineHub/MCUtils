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

public final class GameSetupUtils {

    public static void setupGame() {
        Bootstrap.initialize();
    }

    public static ServerResourceManager loadServerResources() {
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
            Runnable::run,
            Runnable::run
        );
        ServerResourceManager manager = Futures.getUnchecked(completableFuture);
        manager.loadRegistryTags();
        return manager;
    }
}
