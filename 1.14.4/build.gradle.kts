plugins {
    java
    id("fabric-loom") version "0.8.9"
}

group = "org.enginehub"
version = "1.0.0-SNAPSHOT"

dependencies {
    minecraft("com.mojang:minecraft:1.14.4")
    mappings("net.fabricmc:yarn:1.14.4+build.9:v2")
    modImplementation("net.fabricmc:fabric-loader:0.12.8")
    implementation("com.squareup:javapoet:1.13.0")
    val autoServiceVersion = "1.0.1"
    compileOnly("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    annotationProcessor("com.google.auto.service:auto-service:$autoServiceVersion")
}
