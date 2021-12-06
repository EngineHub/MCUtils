rootProject.name = "MCUtils"

include("1.14.4")
include("1.15.2")
include("1.16.5")
include("1.17.1")
include("1.18")

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven {
            name = "Forge Maven"
            url = uri("https://maven.minecraftforge.net/")
            content {
                includeGroupByRegex("net\\.minecraftforge\\..*")
            }
        }
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }
}
