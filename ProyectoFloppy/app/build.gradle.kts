plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")

}

android {
    namespace = "com.example.proyectofloppy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.proyectofloppy"
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
}

dependencies {
    // 1. IMPORTA EL BOM DE FIREBASE (Usa solo la versión más reciente)
    implementation(platform("com.google.firebase:firebase-bom:34.8.0"))

    // 2. LIBRERÍAS DE FIREBASE (El BoM ya sabe qué versión darles)
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")      // Autenticación
    implementation("com.google.firebase:firebase-storage")   // Para guardar los PDFs
    implementation("com.google.firebase:firebase-firestore") // Base de datos para los textos y enlaces

    // 3. DEPENDENCIAS DE ANDROID Y UI
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")

    // 4. PREFERENCIAS DE DATOS
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore-core:1.1.1")
    implementation("com.cloudinary:cloudinary-android:2.3.1")

    // 5. TESTING
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
}