package com.mercadolibre.si_avg_price.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "api.internal")
class ApiConfig {
    lateinit var baseUri: String
    lateinit var clientID: String

}