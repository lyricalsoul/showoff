import com.android.utils.jvmArchitecture
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val ktor_version: String by project
val room_version = "2.7.0-alpha12"

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization")
    id("com.google.devtools.ksp")
    id("androidx.room")
}

kotlin {
    androidTarget()

    jvm("desktop")
    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation("io.ktor:ktor-client-android:$ktor_version")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.androidx.datastore)

            implementation(libs.jewel.int.ui.standalone.x43)
            implementation(libs.jewel.int.ui.decorated.window.x43)
            implementation(libs.icons)
            implementation(compose.desktop.currentOs) {
                exclude(group = "org.jetbrains.compose.material")
            }

            implementation("io.ktor:ktor-client-core:$ktor_version")
            implementation("io.ktor:ktor-client-cio:$ktor_version")
            implementation("io.ktor:ktor-client-websockets:$ktor_version")
            implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
            // slf4j-simple
            implementation(libs.slf4j.simple)

            // lists
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor3)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kdiscordipc)
            implementation(libs.jlayer)

            implementation("io.ktor:ktor-client-java:$ktor_version")
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "io.lyricalsoul"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    buildFeatures {
        compose = true
    }

    defaultConfig {
        applicationId = "io.lyricalsoul"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room {
    println("projectDir: $projectDir")
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.ui.tooling.preview.android)
    ksp("androidx.room:room-compiler:$room_version")
    add("kspDesktop", "androidx.room:room-compiler:$room_version")
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "io.lyricalsoul.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "io.lyricalsoul"
            packageVersion = "1.0.0"
        }
    }

}
