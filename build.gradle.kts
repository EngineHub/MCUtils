plugins {
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT"
}

repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    mavenCentral()
}

loom {
    accessWidenerPath.set(project.file("src/main/resources/mcutils.accesswidener"))
}

dependencies {
    "minecraft"("com.mojang:minecraft:26.2")
    "implementation"("net.fabricmc:fabric-loader:0.19.3")

    "implementation"("com.squareup:javapoet:1.13.0")

    val autoServiceVersion = "1.0.1"
    "compileOnly"("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    "annotationProcessor"("com.google.auto.service:auto-service:$autoServiceVersion")
}

plugins.withId("java") {
    the<JavaPluginExtension>().toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
