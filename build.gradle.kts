// Top-level build file where you can add configuration options common to all sub-projects/modules.

//for imageSlider
buildscript {
    repositories {
        maven ( url = "https://jitpack.io")
    }
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}


plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}