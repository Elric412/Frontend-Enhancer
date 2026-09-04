plugins {
    id("com.android.application")
}

android {
    namespace = "dev.detent.mobile"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.detent.mobile"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0-mobile"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
