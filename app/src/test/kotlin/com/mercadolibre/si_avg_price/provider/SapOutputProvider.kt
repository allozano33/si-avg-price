package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.output.SapOutput
import java.math.BigDecimal

class SapOutputProvider {

    companion object {
        fun get() = SapOutput(
            sku = "1234",
            averageCost = BigDecimal.TEN
        )
    }
}