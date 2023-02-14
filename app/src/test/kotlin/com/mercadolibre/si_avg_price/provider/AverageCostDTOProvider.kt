package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.model.AverageCostDTO
import java.math.BigDecimal

class AverageCostDTOProvider {

    companion object {
        fun get() = AverageCostDTO(
            sku = "1234",
            cnpj = "1234",
            stock = BigDecimal.TEN,
            averagePrice = BigDecimal.TEN
        )
    }
}