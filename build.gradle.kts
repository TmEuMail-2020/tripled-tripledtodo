import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.4.21"
	val springVersion = "2.4.1"
	val springDependencyManagementVersion = "1.0.10.RELEASE"

	idea
	kotlin("jvm") version kotlinVersion apply false
	kotlin("plugin.spring") version kotlinVersion apply false
	id("org.springframework.boot") version springVersion apply false
	id("io.spring.dependency-management") version springDependencyManagementVersion
}

allprojects {
	group = "io.tripled"
	version = "0.0.1-SNAPSHOT"


	repositories {
		mavenCentral()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = JavaVersion.VERSION_1_8.toString()
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

subprojects {
	println("Enabling Spring Boot plugin in project ${project.name}...")
	apply(plugin = "org.springframework.boot")

	println("Enabling Kotlin Spring plugin in project ${project.name}...")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")

	tasks.withType<KotlinCompile> {
		println("Configuring KotlinCompile  $name in project ${project.name}...")
		kotlinOptions {
			languageVersion = "1.4"
			apiVersion = "1.4"
			jvmTarget = "1.8"
			freeCompilerArgs = listOf("-Xjsr305=strict")
		}
	}

	println("Enabling Spring Boot Dependency Management in project ${project.name}...")
	apply(plugin = "io.spring.dependency-management")
	configure<DependencyManagementExtension> {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}