plugins {
    id("fabric-loom") version "0.4.33"
}

repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    mavenCentral()
}

dependencies {
    "minecraft"("com.mojang:minecraft:1.16.1")
    "mappings"("net.fabricmc:yarn:1.16.1+build.21:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.9.0+build.204")

    "implementation"("com.squareup:javapoet:1.13.0")
}
