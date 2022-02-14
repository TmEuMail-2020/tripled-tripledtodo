val kotest_version: String by project

plugins {
    val springVersion = "2.6.3"

    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot") version springVersion
    id("io.spring.dependency-management")
    id("jacoco")
}

dependencies {
    implementation(project(":code:vocabulary"))
    implementation(project(":code:application:api"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}