import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("fabric-loom") version "1.9-SNAPSHOT"
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
    "minecraft"("com.mojang:minecraft:1.21.4-rc3")
    "mappings"(project.the<LoomGradleExtensionAPI>().officialMojangMappings())
    "modImplementation"("net.fabricmc:fabric-loader:0.16.9")

    "implementation"("com.squareup:javapoet:1.13.0")

    val autoServiceVersion = "1.0.1"
    "compileOnly"("com.google.auto.service:auto-service-annotations:$autoServiceVersion")
    "annotationProcessor"("com.google.auto.service:auto-service:$autoServiceVersion")
}

plugins.withId("java") {
    the<JavaPluginExtension>().toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
