package com.mercadolibre.si_avg_price.config.bq

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "topic")
class TopicConfig {
    lateinit var provision: String
    lateinit var unprovision: String
    lateinit var internalProvisionUnprovision: String
}