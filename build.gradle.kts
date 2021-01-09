// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    kotlin("jvm").version("1.4.21")
}

buildscript {
    val kotlin_version by extra("1.3.72")
    repositories {
        google()
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.61")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.5")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:0.10.1")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/tuannq-0847/KarlRecy")
    }
}
