repositories {
    mavenCentral()
}
plugins {
    kotlin("plugin.spring") version "1.6.21"
    id("org.springframework.boot") version "2.7.2"
    id("io.spring.dependency-management") version "1.0.12.RELEASE"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.jpa") version "1.6.21"
}

val springBootVersion = ext.get("springBootVersion") as String

dependencies {
    implementation(project(":domain"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jetty")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("dev.miku:r2dbc-mysql:0.8.2.RELEASE")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("commons-io:commons-io:2.11.0")

    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.newrelic.agent.java:newrelic-api:7.11.1")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.13")
    implementation("io.projectreactor:reactor-core:3.4.21")

    /**
     * Mercado Libre Dependencies
     */
    implementation("com.mercadolibre.metrics:metrics-core:0.0.13")
    implementation("com.mercadolibre.metrics:datadog-metric-wrapper:0.0.13")
    implementation("com.mercadolibre:spring-boot-starter-routing:3.0.0")
    implementation("com.mercadolibre:mqclient:2.1.4")
    implementation("com.mercadolibre:lockclient:3.0.6")

    runtimeOnly("com.mercadolibre:spring-boot-starter:0.0.4")

    /**
     * Testing Dependencies
     */
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("io.r2dbc:r2dbc-h2")

    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:3.1.3")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
}

configurations {
    implementation.get().exclude(module = "spring-boot-starter-tomcat")
    implementation.get().exclude(group = "org.apache.tomcat")
    implementation.get().exclude(group = "org.apache.tomcat.embed")
}

tasks {
    bootJar {
        destinationDirectory.set(file("${rootProject.buildDir}/libs"))
    }
}