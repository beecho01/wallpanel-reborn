import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
val versionBuild = 0 // bump for dog food builds, public betas, etc.

// Optional developer defaults for debug builds, read from local.properties
// (code, hassUrl, broker, brokerUsername, brokerPass). Missing keys are ignored.
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) file.inputStream().use { load(it) }
}

fun localString(key: String): String = "\"${localProperties.getProperty(key, "")}\""

android {
    namespace = "io.github.beecho01.wallpanelreborn"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.github.beecho01.wallpanelreborn"
        minSdk = 24
        targetSdk = 33
        versionCode = versionMajor * 10000 + versionMinor * 1000 + versionPatch * 100 + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "DEV_CODE", localString("code"))
        buildConfigField("String", "DEV_HASS_URL", localString("hassUrl"))
        buildConfigField("String", "DEV_BROKER", localString("broker"))
        buildConfigField("String", "DEV_BROKER_USERNAME", localString("brokerUsername"))
        buildConfigField("String", "DEV_BROKER_PASS", localString("brokerPass"))
    }

    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    packaging {
        resources {
            excludes += listOf("META-INF/INDEX.LIST", "META-INF/io.netty.versions.properties")
        }
    }

    lint {
        lintConfig = file("lint.xml")
    }
}

dependencies {
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.fragment)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.livedata)
    implementation(libs.androidx.lifecycle.service)
    implementation(libs.androidx.localbroadcastmanager)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.webkit)

    implementation(libs.play.services.vision)

    implementation(libs.dagger)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    ksp(libs.dagger.compiler)
    ksp(libs.dagger.android.processor)

    implementation(libs.hivemq.mqtt.client)
    implementation(libs.androidasync)
    implementation(libs.timber)
    implementation(libs.glide)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.test.runner)
}
