val kotest_version: String by project

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":application:api"))
    implementation(project(":vocabulary"))
    implementation(project(":application:domain"))

    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-property:$kotest_version")
}