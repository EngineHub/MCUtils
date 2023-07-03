plugins {
    java
    id("fabric-loom") version "0.13.9"
}

group = "org.enginehub"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(mapOf("path" to ":core")))
    minecraft("com.mojang:minecraft:1.14.4")
    mappings("net.fabricmc:yarn:1.14.4+build.9:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.property("fabric_loader.version")}")
    implementation("com.squareup:javapoet:${project.property("javapoet.version")}")
    compileOnly("com.google.auto.service:auto-service-annotations:${project.property("autoservice.version")}")
    annotationProcessor("com.google.auto.service:auto-service:${project.property("autoservice.version")}")
}
