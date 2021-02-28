val kotest_version: String by project

plugins {
    val springVersion = "2.4.1"

    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot") version springVersion
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":code:vocabulary"))
    implementation(project(":code:application:api"))
    implementation(project(":code:application:usecases"))
    implementation(project(":code:application:domain"))

    implementation(project(":code:infrastructure:incoming:rest"))
    implementation(project(":code:infrastructure:incoming:validation"))

    implementation(project(":code:infrastructure:outgoing:postgres"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("io.zonky.test:embedded-postgres:1.2.6")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
    testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
    testImplementation("io.kotest:kotest-property:$kotest_version")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}