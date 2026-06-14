plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()

    // Menggunakan "desktop" agar sesuai dengan konfigurasi compose.desktop di bawah
    jvm("desktop")

    sourceSets {
        val commonMain by getting {
            dependencies {
                // UI & Navigation
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.navigation.compose)

                // Lifecycle & State
                implementation(libs.lifecycle.runtime)
                implementation(libs.lifecycle.viewmodel)
                implementation(libs.savedstate)

                // SQLDelight & Coroutines
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutine)
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")

                // DataStore & Datetime
                implementation(libs.androidx.datastore.preferences.core)
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

                // Koin DI
                implementation("io.insert-koin:koin-core:3.5.3")
                implementation("io.insert-koin:koin-compose:1.1.2")
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.activity)
                implementation("app.cash.sqldelight:android-driver:2.0.1")
                implementation("io.insert-koin:koin-android:3.5.3")
                implementation("io.insert-koin:koin-androidx-compose:3.5.3")
            }
        }

        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.coroutines.swing)
                implementation("app.cash.sqldelight:sqlite-driver:2.0.1")
            }
        }
    }
}

sqldelight {
    databases {
        create("NotesDatabase") {
            packageName.set("com.example.pengembanganaplikasimobile.db")
        }
    }
}

android {
    namespace = "com.example.pengembanganaplikasimobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.pengembanganaplikasimobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}

compose.desktop {
    application {
        mainClass = "com.example.pengembanganaplikasimobile.MainKt"
    }
}