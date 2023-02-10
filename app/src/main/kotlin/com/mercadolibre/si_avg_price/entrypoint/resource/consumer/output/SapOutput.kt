package com.mercadolibre.si_avg_price.entrypoint.resource.consumer.output

import java.math.BigDecimal

class SapOutput(
    val sku: String,
    val averageCost: BigDecimal
)