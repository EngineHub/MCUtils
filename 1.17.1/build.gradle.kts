plugins {
    java
    id("fabric-loom") version "0.10-SNAPSHOT"
}

group = "org.enginehub"
version = "1.0.0-SNAPSHOT"

dependencies {
    minecraft("com.mojang:minecraft:1.17.1")
    mappings("net.fabricmc:yarn:1.17.1+build.2:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader.version")}")
    implementation("com.squareup:javapoet:${project.property("javapoet.version")}")
    compileOnly("com.google.auto.service:auto-service-annotations:${project.property("autoservice.version")}")
    annotationProcessor("com.google.auto.service:auto-service:${project.property("autoservice.version")}")
}
