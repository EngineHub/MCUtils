plugins {
    java
}

group = "org.enginehub"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup:javapoet:${project.property("javapoet.version")}")
    compileOnly("com.google.auto.service:auto-service-annotations:${project.property("autoservice.version")}")
    annotationProcessor("com.google.auto.service:auto-service:${project.property("autoservice.version")}")
}
