plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.umc.timeCAlling"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.umc.timeCAlling"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //gson
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.gson)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)

    // https://github.com/square/okhttp
    implementation(libs.okhttp)

    // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
    implementation(libs.logging.interceptor)

    //lifecycle
    implementation (libs.androidx.lifecycle.extensions)

    //viewModel
    implementation (libs.androidx.lifecycle.viewmodel.ktx)
    implementation (libs.androidx.lifecycle.runtime.ktx)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.fragment.ktx)

    // Glide Image Loading Library
    implementation(libs.glide)
    ksp(libs.ksp)

    //CardView
    implementation (libs.androidx.cardview)

    // Coroutines Dependency
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //indicator : https://github.com/tommybuonomo/dotsindicator?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=7127
    implementation(libs.dotsindicator)

    implementation(libs.material.calendarview){exclude(group = "com.android.support")}
    implementation (libs.number.picker)

    //Tmap
    implementation(files("libs/tmap-sdk-1.8.aar"))
    implementation(files("libs/vsm-tmap-sdk-v2-android-1.7.23.aar"))

    implementation(libs.play.services.location)
}
configurations.all {
    exclude(group = "com.android.support")
}