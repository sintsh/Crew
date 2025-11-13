plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("androidx.room") version "2.8.3"
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.crew"
    compileSdk = 36


    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    defaultConfig {
        applicationId = "com.example.crew"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.crew.HiltTestRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }


}

dependencies {
    //implementations
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.rxjava2)
    implementation(libs.androidx.room.rxjava3)
    implementation(libs.androidx.room.guava)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.gson)





// --- Local Unit Tests (test folder) ---
    testImplementation(libs.androidx.room.testing)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.mockk)
    testImplementation(libs.junit.platform.launcher)

// --- Android Instrumentation Tests (androidTest folder) ---
    androidTestImplementation(libs.androidx.junit) // androidx.test.ext:junit
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation("androidx.test:rules:1.5.0")
//    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:core:1.6.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")

// Hilt Testing - The Corrected Part
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.57.2")
    kspAndroidTest("com.google.dagger:hilt-compiler:2.57.2") // Correctly uses kspAndroidTest

// Mockito for Instrumentation Tests
    androidTestImplementation("org.mockito:mockito-core:5.5.0")
    androidTestImplementation("org.mockito:mockito-android:5.5.0")

// Fragment and Navigation Testing
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.5")


    testImplementation("androidx.arch.core:core-testing:2.2.0")

    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")


    //room paging
    implementation(libs.androidx.room.paging)
}

configurations.all {
    resolutionStrategy {
        force(
            "androidx.test:core:1.6.1",
            "androidx.test:runner:1.6.2",
            "androidx.test:rules:1.6.1",
            "androidx.test.espresso:espresso-core:3.6.1",
            "androidx.test.ext:junit:1.2.1"
        )
    }
}


room {
    schemaDirectory("$projectDir/schemas")
}