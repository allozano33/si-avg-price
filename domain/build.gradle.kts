tasks {
    withType(Jar::class) {
        enabled = true
    }
}

dependencies {
    implementation("javax.inject:javax.inject:1")
    implementation("org.slf4j:slf4j-api:1.7.36")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
}