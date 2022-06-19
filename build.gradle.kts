// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    kotlin("jvm").version("1.6.10")
}

buildscript {
    val kotlin_version by extra("1.3.72")
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    }
}