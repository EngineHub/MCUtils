plugins {
    id("fabric-loom") version "0.13.20"
}

dependencies {
    implementation(project(mapOf("path" to ":core")))
    minecraft("com.mojang:minecraft:1.15.2")
    mappings("net.fabricmc:yarn:1.15.2+build.9:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader.version")}")
}
