package com.mercadolibre.si_avg_price

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.core.env.AbstractEnvironment

@SpringBootApplication
class Application

fun main(args: Array<String>) {

    (System.getenv("SCOPE") ?: setupDevelopmentProfile()).let {
        val profile = it.split("-").last()
        System.setProperty(
            AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME,
            profile
        )
    }
    runApplication<Application>(*args)
}

private fun setupDevelopmentProfile(): String {
    return "development"
}
