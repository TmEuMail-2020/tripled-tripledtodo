import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val kotlinVersion = "1.4.21"
	val springDependencyManagementVersion = "1.0.10.RELEASE"

	idea
	jacoco
	kotlin("jvm") version kotlinVersion apply false
	kotlin("plugin.spring") version kotlinVersion apply false
	id("io.spring.dependency-management") version springDependencyManagementVersion
	id("io.gitlab.arturbosch.detekt").version("1.16.0-RC1")
	id("org.kordamp.gradle.kotlin-project") version "0.43.0"
	id("org.kordamp.gradle.coveralls") version "0.43.0"
	id("org.kordamp.gradle.jacoco") version "0.43.0"

}

config {

	release = false

	info {
		name = "triple-todo"
		description = "Example application for clean architecture, ddd, cqrs, tdd, etc"
		inceptionYear = "2020"
		vendor = "tripled"
		group = "io.tripled"

		links {
			website = "https://www.tripled.io"
			scm = "https://gitlab.rotate-it.be/tripled/triple-todo"
			issueTracker = "https://gitlab.rotate-it.be/tripled/triple-todo/-/issues"
		}

		licensing {
			licenses {
				license {
					id = "Apache-2.0"
				}
			}
		}

		coverage {
			jacoco {
				enabled = true
				aggregateReportHtmlFile = File("build/reports/index.html")
			}
			coveralls {
				enabled = true
			}
		}

		people {
			person {
				id = "gert"
				name = "Gert Vilain"
				email = "gert@tripled.io"
				roles = listOf("developer")
			}
			person {
				id = "kris"
				name = "Kris Hofmans"
				email = "kris@tripled.io"
				roles = listOf("author", "developer")
			}
		}
	}
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