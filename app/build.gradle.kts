plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.darke.habithive"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.darke.habithive"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.add("-Xlint:deprecation")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.facebook.android.sdk)
    implementation(libs.firebase.firestore)
    implementation(libs.recyclerview)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.fragment.testing)
    implementation(libs.material.v130alpha03)
    implementation(libs.material.calendarview)
    implementation(libs.androidx.espresso.intents)
    implementation(libs.threetenabp)
    implementation(libs.androidx.work.runtime)
    testImplementation("junit:junit:4.13.2")
    testImplementation(libs.junit.v113)
    testImplementation(libs.espresso.core.v340)
    testImplementation(libs.truth)
    testImplementation(libs.mockito.core)
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation(libs.androidx.core.testing)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.4.0")
    androidTestImplementation("androidx.test:core:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation(libs.mockito.android)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.v2101)
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}