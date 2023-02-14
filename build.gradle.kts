import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version "1.7.10"
	id("idea")
	jacoco
}

group = "com.mercadolibre"
version = "0.0.1-SNAPSHOT"

allprojects {

	apply(plugin = "kotlin")
	apply(plugin = "jacoco")

	ext {
		set("springBootVersion", "2.7.2")
	}

	jacoco {
		toolVersion = "0.8.8"
	}

	repositories {
		maven {
			url = uri("https://maven.artifacts.furycloud.io/repository/all")
			isAllowInsecureProtocol = true
		}
	}
}

subprojects {

	java {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	tasks {
		withType<KotlinCompile> {
			kotlinOptions {
				freeCompilerArgs = listOf("-Xjsr305=strict")
				jvmTarget = "17"
			}
		}
		withType<Test> {
			this.systemProperty("configFileName", "src/test/resources/application.yml")
			this.systemProperty("checksumEnabled", "false")
			environment("APPLICATION", "si-avg-price")
			useJUnitPlatform()
		}
	}

	dependencies {
		implementation("com.newrelic.agent.java:newrelic-api:7.11.1")

		testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
		testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
		testImplementation("io.mockk:mockk:1.12.4")
	}
}

tasks {

	getByName<JacocoReport>("jacocoTestReport") {
		description = "Generates an aggregate report from all subprojects"
		group = "verification"
		dependsOn(subprojects.map { it.tasks["test"] })
		sourceDirectories.setFrom(files(subprojects.flatMap { it.sourceSets["main"].allSource.srcDirs }))
		classDirectories.setFrom(files(subprojects.flatMap { it.sourceSets["main"].output }))
		additionalSourceDirs.setFrom(files(subprojects.flatMap { it.sourceSets["main"].allSource.srcDirs }))
		executionData.setFrom(files(subprojects.map { File(it.buildDir, "/jacoco/test.exec") }))

		reports {
			html.required.set(true)
			xml.required.set(true)
		}

		afterEvaluate {
			val excludedClasses = listOf(
				"**/com/mercadolibre/si_avg_price/Application**",
				"**/com/mercadolibre/si_avg_price/config/**",
				"**/com/mercadolibre/si_avg_price/extension/**",
				"**/com/mercadolibre/si_avg_price/entrypoint/resource/**",
				"**/com/mercadolibre/si_avg_price/model/**",
				"**/com/mercadolibre/si_avg_price/gateway/resource/**",
				"**/com/mercadolibre/si_avg_price/gateway/mock/",
				"**/com/mercadolibre/si_avg_price/repository/**",
				"**/com/mercadolibre/si_avg_price/exception/**",
				"**/com/mercadolibre/si_avg_price/entity/**",
				"**/com/mercadolibre/si_avg_price/gateway/metric/**"
			)

			val coverageParticipants = classDirectories.files
				.map { fileTree(it).exclude(excludedClasses) }
				.let { files(it) }

			classDirectories.setFrom(coverageParticipants)
		}
	}
}

task("bootJar").apply {
	group = "build"
	dependsOn(":app:bootJar")
}

task("bootRun").apply {
	group = "application"
	dependsOn(":app:bootRun")
}