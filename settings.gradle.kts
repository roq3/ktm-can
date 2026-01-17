pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    
    plugins {
        kotlin("jvm") version "1.9.22"
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

rootProject.name = "ktm-can-lib"
