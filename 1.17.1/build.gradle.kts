import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("fabric-loom") version "0.13.20"
}

dependencies {
    implementation(project(mapOf("path" to ":core")))
    minecraft("com.mojang:minecraft:1.17.1")
    mappings(project.the<LoomGradleExtensionAPI>().officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader.version")}")
}
