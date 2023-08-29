plugins {
    java
}

allprojects {
    apply<JavaPlugin>()

    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

subprojects {
    dependencies {
        implementation("com.google.guava:guava:31.1-jre")
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("com.squareup:javapoet:${project.property("javapoet.version")}")
        compileOnly("com.google.auto.service:auto-service-annotations:${project.property("autoservice.version")}")
        annotationProcessor("com.google.auto.service:auto-service:${project.property("autoservice.version")}")
    }
}
