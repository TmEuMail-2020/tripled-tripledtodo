dependencies {
    implementation(project(":code:application:api"))
    implementation(project(":code:vocabulary"))
    implementation(project(":code:application:domain"))

    testImplementation("io.kotest:kotest-runner-junit5")
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-property")
}