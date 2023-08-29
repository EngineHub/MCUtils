rootProject.name = "MCUtils"

include("core")
include("1.14.4")
include("1.15.2")
include("1.16.5")
include("1.17.1")
include("1.18.2")
include("1.19.4")
include("1.20.1")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }
}
