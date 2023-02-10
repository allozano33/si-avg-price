package com.mercadolibre.si_avg_price.gateway

interface DatadogGateway {

    fun incrementMetric(
        name: String,
        extraTags: Map<String, String>? = emptyMap()
    )

    fun gauge(key: String, value: Long, extraTags: Map<String, String>?)
}
