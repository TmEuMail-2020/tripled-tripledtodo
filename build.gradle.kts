import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.4.31"
	val springDependencyManagementVersion = "1.0.10.RELEASE"

	idea
	jacoco
	kotlin("jvm") version kotlinVersion apply false
	kotlin("plugin.spring") version kotlinVersion apply false
	id("io.spring.dependency-management") version springDependencyManagementVersion
	id("org.barfuin.gradle.jacocolog") version "1.2.3"
	id("io.gitlab.arturbosch.detekt").version("1.17.0")
}

allprojects {
	group = "io.tripled"
	version = "0.0.1-SNAPSHOT"

	repositories {
		mavenCentral()
		jcenter()
	}

	//apply(plugin = "io.gitlab.arturbosch.detekt")
	apply(plugin = "jacoco")

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			languageVersion = "1.4"
			apiVersion = "1.4"
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_1_8.toString()
		}
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

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}