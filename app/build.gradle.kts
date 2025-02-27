plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "2.0.21"
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.appinstagram"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.appinstagram"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
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
    implementation ("com.google.android.material:material:1.11.0")
    //Room DB
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    //Navigation Component
    val nav_version = "2.8.6"

    // Jetpack Compose integration
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    //Circle Image
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    //Glide
    implementation ("com.github.bumptech.glide:glide:4.16.0")

    //CircleIndicator
    implementation ("me.relex:circleindicator:2.1.6")
    //koin
    implementation ("io.insert-koin:koin-android:3.3.2")

    //SmartRefreshLayout
    // Note: There will be no default Header and Footer after subcontracting. It needs to be added manually!

    //MVVM LifeCycle
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")


// To search lastest version by https://search.maven.org/search?q=g:io.github.scwang90
    implementation ("androidx.appcompat:appcompat:1.0.0")
    implementation ("io.github.scwang90:refresh-layout-kernel:3.0.0-alpha")     //core
    implementation ("io.github.scwang90:refresh-header-classics:3.0.0-alpha")   //ClassicsHeader
    implementation ("io.github.scwang90:refresh-header-radar:3.0.0-alpha")      //BezierRadarHeader
    implementation ("io.github.scwang90:refresh-header-falsify:3.0.0-alpha")    //FalsifyHeader
    implementation ("io.github.scwang90:refresh-header-material:3.0.0-alpha")   //MaterialHeader
    implementation ("io.github.scwang90:refresh-header-two-level:3.0.0-alpha")  //TwoLevelHeader
    implementation ("io.github.scwang90:refresh-footer-ball:3.0.0-alpha")       //BallPulseFooter
    implementation ("io.github.scwang90:refresh-footer-classics:3.0.0-alpha")   //ClassicsFooter

    //MMKV giúp lưu dữ liệu nhanh hơn sharePreference
    implementation ("com.tencent:mmkv:2.1.0")

    //OkHttp
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

}