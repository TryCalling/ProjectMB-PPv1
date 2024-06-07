pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        //I'm missing it, If need ImageSlider, Pls add library in it.
        maven("https://jitpack.io")
    }
}

//for ImageSlider Kotlin
rootProject.name = "Auto_ImageSlider_kotlin_android"
include (":app")

rootProject.name = "ProjectMB-PP"
include(":app")
 