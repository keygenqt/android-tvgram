pluginManagement {

    val kotlinVersion: String by settings
    val gradleVersion: String by settings
    val spotlessVersion: String by settings

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    plugins {
        id("org.jetbrains.kotlin.android") version kotlinVersion
        id("com.android.application") version gradleVersion
        id("com.android.library") version gradleVersion

        kotlin("kapt") version kotlinVersion

        // https://github.com/Kotlin/kotlinx.serialization
        kotlin("plugin.serialization") version kotlinVersion
        // https://github.com/diffplug/spotless
        id("com.diffplug.spotless") version spotlessVersion
    }
}

// https://docs.gradle.org/current/userguide/platforms.html#sub:central-declaration-of-dependencies
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(fileTree("dependencies"))
        }
    }
}

rootProject.name = "Tvgram"
include(":app")
include(":libtd")
