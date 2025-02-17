import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}
val properties = Properties().apply{
    load(FileInputStream(rootProject.file("local.properties")))
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
        buildConfigField("String", "KAKAO_APP_KEY", "\"${properties["KAKAO_APP_KEY"]}\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            manifestPlaceholders["KAKAO_APP_KEY"] = properties["KAKAO_APP_KEY"] as String
        }

        release {
            manifestPlaceholders += mapOf()
            isMinifyEnabled = false
            manifestPlaceholders["KAKAO_APP_KEY"] = properties["KAKAO_APP_KEY"] as String
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3.android)
    implementation(libs.firebase.messaging.ktx)
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

    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")

    //CardView
    implementation (libs.androidx.cardview)

    // circle ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")

    // Coroutines Dependency
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //indicator : https://github.com/tommybuonomo/dotsindicator?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=7127
    implementation(libs.dotsindicator)

    implementation(libs.material.calendarview) {exclude(group = "com.android.support")}
    implementation (libs.number.picker)

    //Tmap
    implementation(files("libs/tmap-sdk-1.8.aar"))
    implementation(files("libs/vsm-tmap-sdk-v2-android-1.7.23.aar"))

    implementation(libs.play.services.location)

    implementation(libs.android.colorpicker)

    implementation(libs.threetenabp)

    implementation ("de.hdodenhof:circleimageview:3.1.0") // CircularImageView 라이브러리

    // 카카오 로그인 SDK
    implementation ("com.kakao.sdk:v2-user:2.20.6")

    //로깅
    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("com.auth0:java-jwt:3.19.2")

    implementation ("com.auth0.android:jwtdecode:2.0.2")

    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
}
configurations.all {
    exclude(group = "com.android.support")
}