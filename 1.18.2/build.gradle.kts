import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    java
    id("fabric-loom") version "0.13.20"
}

group = "org.enginehub"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(mapOf("path" to ":core")))
    minecraft("com.mojang:minecraft:1.18.2")
    mappings(project.the<LoomGradleExtensionAPI>().officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader.version")}")
    implementation("com.squareup:javapoet:${project.property("javapoet.version")}")
    compileOnly("com.google.auto.service:auto-service-annotations:${project.property("autoservice.version")}")
    annotationProcessor("com.google.auto.service:auto-service:${project.property("autoservice.version")}")
}
