import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("fabric-loom") version "0.12.47"
}

repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    mavenCentral()
}

dependencies {
    "minecraft"("com.mojang:minecraft:1.19")
    "mappings"(project.the<LoomGradleExtensionAPI>().officialMojangMappings())
    "modImplementation"("net.fabricmc:fabric-loader:0.14.6")

    "implementation"("com.squareup:javapoet:1.13.0")

    val autoServiceVersion = "1.0.1"
    "compileOnly"("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    "annotationProcessor"("com.google.auto.service:auto-service:$autoServiceVersion")
}
