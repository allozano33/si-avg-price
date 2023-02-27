package com.mercadolibre.si_avg_price.config.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

annotation class RefreshScope


@Configuration
@ConfigurationProperties
@RefreshScope
class SellerIdentificationProcessConfig {
    var sellerIdentificationToProcess: List<Long> = emptyList()
}

