import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.4.21"
	val springDependencyManagementVersion = "1.0.10.RELEASE"

	idea
	kotlin("jvm") version kotlinVersion apply false
	kotlin("plugin.spring") version kotlinVersion apply false
	id("io.spring.dependency-management") version springDependencyManagementVersion
	id("jacoco")
	id("io.gitlab.arturbosch.detekt").version("1.16.0-RC1")
}

allprojects {
	group = "io.tripled"
	version = "0.0.1-SNAPSHOT"


	repositories {
		mavenCentral()
		jcenter()
	}

	apply(plugin = "io.gitlab.arturbosch.detekt")

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			languageVersion = "1.4"
			apiVersion = "1.4"
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_1_8.toString()
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}