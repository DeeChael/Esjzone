import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
}

val app_version_index = project.properties["app_version_index"].toString().toInt()
val app_version = project.properties["app_version"].toString()

android {
    namespace = "net.deechael.esjzone"
    compileSdk = 34

    defaultConfig {
        applicationId = "net.deechael.esjzone"
        minSdk = 29
        targetSdk = 34
        versionCode = app_version_index
        versionName = app_version

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    defaultConfig {
        val env = System.getenv()
        val version = if (env.containsKey("BUILD_VERSION")) {
            env["BUILD_VERSION"]!!
        } else {
            app_version
        }
        val commit = if (env.containsKey("COMMIT_ID")) {
            env["COMMIT_ID"]!!
        } else {
            "Unreachable"
        }
        buildConfigField("String", "APP_VERSION", "\"$version\"")
        buildConfigField("String", "BUILD_DATE", "\"${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}\"")
        buildConfigField("String", "COMMIT_ID", "\"$commit\"")
    }
}

dependencies {
    implementation(libs.okhttp)
    implementation(libs.coil.core)
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.voyager.navigator)
    implementation(libs.voyager.screenmodel)
    implementation(libs.voyager.bottomSheetNavigator)
    implementation(libs.voyager.tabNavigator)
    implementation(libs.voyager.transitions)
    implementation(libs.gson)
    implementation(libs.jsoup)
    implementation(libs.xsoup)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.iconsExtended)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.navigation)

    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}