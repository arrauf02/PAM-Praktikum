plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.sqldelight)
}

kotlin {
    androidTarget()

    jvm()

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)

                implementation(libs.lifecycle.runtime)
                implementation(libs.lifecycle.viewmodel)

                implementation(libs.navigation.compose)
                implementation(libs.savedstate)
                implementation(libs.sqldelight.runtime)
                implementation(libs.sqldelight.coroutine)
                implementation(libs.androidx.datastore.preferences.core)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core)
                implementation(libs.androidx.activity)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.coroutines.swing)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
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

tasks.register("jvmRun") {
    dependsOn("run")
}
