plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    kotlin("kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.weatherappsample1yt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.weatherappsample1yt"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

kapt {
    correctErrorTypes = true
    arguments {
        arg("dagger.fastInit", "true")
        arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
        arg("dagger.hilt.android.internal.projectType", "1")
        arg("dagger.hilt.internal.useAggregatingRootProcessor", "true")
    }
}


//
dependencies{
    //retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //okHttp
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //gson
    implementation (libs.gson)

    implementation(libs.glide)
    implementation (libs.blurview)
    implementation (libs.weatherview)

    implementation(libs.symbol.processing.api)

    implementation (libs.play.services.location)
    implementation (libs.androidx.viewpager2)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.fragment.ktx)

    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)

    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support) // if you use the support libraries
    kapt(libs.dagger.android.processor)

    implementation(libs.androidx.databinding.runtime)

    implementation(libs.androidx.datastore.preferences)

    implementation(libs.play.services.location)
    implementation (libs.kotlinx.coroutines.play.services)

    implementation (libs.lottie)
    implementation (libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.inline)
    testImplementation(libs.androidx.core)
    
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

