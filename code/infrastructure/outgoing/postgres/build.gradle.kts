val kotest_version: String by project

plugins {
    val springVersion = "2.4.1"

    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot") version springVersion
    id("io.spring.dependency-management")
    id("jacoco")
}

dependencies {
    implementation(project(":code:vocabulary"))
    implementation(project(":code:application:domain"))

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.jetbrains.exposed:exposed-core:0.29.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.29.1")
    implementation("io.zonky.test:embedded-postgres:1.2.6")

    testImplementation("org.springframework.boot:spring-boot-starter-jdbc")
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