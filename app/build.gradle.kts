plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    id("kotlin-kapt")
}

android {
    compileSdk = 30
    buildToolsVersion = "30.0.1"
    defaultConfig {
        minSdk = 21
        targetSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
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
    kotlinOptions.apply {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

//tasks {
//    val dokka by getting(org.jetbrains.dokka.gradle.DokkaTask::class) {
//        outputFormat = "html"
//        outputDirectory = "$projectDir/../docs/"
//    }
//}

//apply(from = rootProject.file("gradle/publish.gradle"))

tasks {
//    val dokkaJavadoc by creating(org.jetbrains.dokka.gradle.DokkaTask::class) {
//        outputFormat = "javadoc"
//        outputDirectory = "$buildDir/javadoc"
//    }

    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(android.sourceSets.getByName("main").java.srcDirs)
    }

//    val javadocJar by creating(Jar::class) {
//        dependsOn.add(dokkaJavadoc)
//        archiveClassifier.set("javadoc")
//        from(dokkaJavadoc.outputDirectory)
//    }
}

artifacts {
    archives(tasks.getByName("sourcesJar"))
}

dependencies {
    val paging_version = "3.0.0-alpha11"
    implementation("androidx.paging:paging-runtime-ktx:$paging_version")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.21")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.recyclerview:recyclerview:1.2.0-beta01")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")
}