plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")

    kotlin("plugin.serialization")
    kotlin("kapt")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.keygenqt.tvgram"
        minSdk = 26
        targetSdk = 32
        versionCode = 1
        versionName = "0.0.1"

        // App api_id telegram api
        buildConfigField(
            "Integer",
            "API_ID",
            findProperty("api_id").toString()
        )

        // App api_hash telegram api
        buildConfigField(
            "String",
            "API_HASH",
            """"${findProperty("api_hash")}""""
        )
    }

    buildFeatures {
        viewBinding = true
    }

    // division resources
    sourceSets {
        getByName("main").let { data ->
            data.res.setSrcDirs(emptySet<String>())
            file("src/main/res").listFiles()?.toList()?.forEach { dir ->
                data.res.srcDir(dir)
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(project(":libtd"))
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.other)
    kapt(libs.bundles.hiltKapt)
}