import java.util.Properties

plugins {
    id("com.android.application") version "8.6.1"
    id("org.jetbrains.kotlin.android") version "1.9.10"
}

android {
    namespace = "com.example.runningapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.runningapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { inputStream ->
                localProperties.load(inputStream)
            }
        }
        buildConfigField("String", "NAVER_MAPS_CLIENT_ID", "\"${localProperties.getProperty("com.naver.maps.map.CLIENT_ID")}\"")
    }

    buildFeatures {
        dataBinding = true
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
    viewBinding.isEnabled = true

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.naver.maps:map-sdk:3.19.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.3")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.databinding.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}