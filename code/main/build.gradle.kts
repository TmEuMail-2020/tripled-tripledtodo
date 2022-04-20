plugins {
    id("jacoco-report-aggregation")
}

dependencies {
    implementation(project(":code:vocabulary"))
    implementation(project(":code:application:api"))
    implementation(project(":code:application:usecases"))
    implementation(project(":code:application:domain"))

    implementation(project(":code:infrastructure:incoming:rest"))
    implementation(project(":code:infrastructure:incoming:validation"))

    implementation(project(":code:infrastructure:outgoing:sqldb"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.liquibase:liquibase-core")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-property")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
}