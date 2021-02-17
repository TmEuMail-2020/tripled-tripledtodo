val kotest_version: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":code:vocabulary"))
}