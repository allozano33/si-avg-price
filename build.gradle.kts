import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.1"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
	jacoco
}

group = "com.mercadolibre"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	maven("https://maven.artifacts.furycloud.io/repository/all")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web") {
		exclude(module = "spring-boot-starter-tomcat")
	}
	implementation("org.springframework.boot:spring-boot-starter-jetty")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("com.newrelic.agent.java:newrelic-api:5.13.0")
	implementation("com.mercadolibre.metrics:metrics-core:0.0.13")
	implementation("com.mercadolibre.metrics:datadog-metric-wrapper:0.0.13")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<JacocoReport> {
	reports {
		xml.isEnabled = true
		html.isEnabled = true
	}
	
	afterEvaluate {
		val coverageParticipants = classDirectories.files
			.map { fileTree(it).exclude("**/ApplicationKt.class") }
			.let { files(it) }
		classDirectories.setFrom(coverageParticipants)
	}
}
