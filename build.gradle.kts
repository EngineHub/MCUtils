plugins {
    id("fabric-loom") version "0.8.9"
}

repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    mavenCentral()
}

dependencies {
    "minecraft"("com.mojang:minecraft:1.17")
    "mappings"("net.fabricmc:yarn:1.17+build.1:v2")
    "modImplementation"("net.fabricmc:fabric-loader:0.11.3")

    "implementation"("com.squareup:javapoet:1.13.0")

    val autoServiceVersion = "1.0"
    "compileOnly"("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    "annotationProcessor"("com.google.auto.service:auto-service:$autoServiceVersion")
}
