import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.plugin.kotlinToolingVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
	val kotlinVersion = "1.8.20"
	val springDependencyManagementVersion = "1.0.11.RELEASE"

	kotlin("jvm") version kotlinVersion
	kotlin("plugin.spring") version kotlinVersion
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version springDependencyManagementVersion

	id("org.barfuin.gradle.jacocolog") version "2.0.0"
	id("io.gitlab.arturbosch.detekt") version "1.17.0"
}

allprojects {
	group = "io.tripled"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
	}

	apply {
		plugin("io.spring.dependency-management")
		plugin("org.jetbrains.kotlin.plugin.spring")
		plugin("kotlin")
		plugin("jacoco")
	}

	dependencyManagement {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		}
	}

	val kotest_version: String by project

	dependencies {
		testImplementation("io.kotest:kotest-runner-junit5:$kotest_version")
		testImplementation("io.kotest:kotest-assertions-core:$kotest_version")
		testImplementation("io.kotest:kotest-property:$kotest_version")
		testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
		testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
	}

	tasks.withType<KotlinCompile> {
//		sourceCompatibility = JavaVersion.VERSION_17.toString()
//		targetCompatibility = JavaVersion.VERSION_17.toString()

		kotlinOptions {
			languageVersion = "1.8"
			apiVersion = "1.8"
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_17.toString()
		}
	}

	tasks.withType<BootJar> {
		enabled = false
	}

	tasks.withType<JacocoCoverageVerification> {
		violationRules {
			rule {
				limit {
					val thisIsTheWay = "1.0"
					minimum = thisIsTheWay.toBigDecimal()
				}
			}
		}
	}

	tasks.withType<JacocoReport> {
		reports {
			xml.required.set(true)
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()

		testLogging {
			events(
				PASSED,
				SKIPPED,
				FAILED
			)
			showCauses = true
			showExceptions = true
			showStackTraces = true
			exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
		}
	}
	
}