package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import java.math.BigDecimal
import java.time.LocalDateTime

class AveragePriceProcessProvider {

    companion object {
        fun get(
            sku: String = "1234",
            cnpj: String = "1234",
            stock: BigDecimal = BigDecimal.TEN,
            averagePrice: BigDecimal = BigDecimal.TEN,
            dateUpdate: LocalDateTime = LocalDateTime.now()
        ) = AveragePriceProcess(
            sku = sku,
            cnpj = cnpj,
            stock = stock,
            averagePrice = averagePrice,
            dateUpdate = dateUpdate
        )
    }
}