plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.cycling.allainovel"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.cycling.allainovel"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core-ui"))
    implementation(project(":core-common"))
    implementation(project(":core-database"))
    implementation(project(":core-network"))
    implementation(project(":core-datastore"))
    implementation(project(":feature-book"))
    implementation(project(":feature-settings"))
    implementation(project(":feature-editor"))
    implementation(project(":feature-chapter"))
    implementation(project(":feature-ai"))
    implementation(project(":feature-outline"))
    implementation(project(":feature-character"))
    implementation(project(":feature-worldbuilding"))
    implementation(project(":feature-statistics"))
    implementation(project(":feature-tools"))
    implementation(project(":feature-oneclick"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

detekt {
    config.setFrom("$rootDir/detekt.yml")
    buildUponDefaultConfig = false
}

ktlint {
    android.set(true)
    outputColorName.set("RED")
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
    }
}
