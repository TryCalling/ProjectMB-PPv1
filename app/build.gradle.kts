plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.projectmb_pp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projectmb_pp"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    //first time
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Gradle that updated
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.dmytrodanylyk.circular-progress-button:library:1.1.3")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.google.firebase:firebase-messaging:24.0.0") // Check for latest version
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
    androidTestImplementation("com.android.support.test:runner:1.0.2")

    //Gradle for RecyclerView
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("com.squareup.moshi:moshi-kotlin:1.11.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    
    //Gradle for play VDO
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("androidx.preference:preference-ktx:1.2.1")



}