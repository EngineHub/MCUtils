plugins {
    id("fabric-loom") version "0.4-SNAPSHOT"
}

repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    mavenCentral()
}

dependencies {
    "minecraft"("com.mojang:minecraft:1.16.2")
    "mappings"("net.fabricmc:yarn:1.16.2+build.19:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.9.1+build.205")

    "implementation"("com.squareup:javapoet:1.13.0")

    val autoServiceVersion = "1.0-rc7"
    "compileOnly"("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    "annotationProcessor"("com.google.auto.service:auto-service:$autoServiceVersion")
}
