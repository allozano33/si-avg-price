package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import java.math.BigDecimal

class AveragePriceProcessProvider {

    companion object {
        fun get() = AveragePriceProcess(
            sku = "1234",
            cnpj = "1234",
            stock = BigDecimal.TEN,
            averagePrice = BigDecimal.TEN
        )
    }
}