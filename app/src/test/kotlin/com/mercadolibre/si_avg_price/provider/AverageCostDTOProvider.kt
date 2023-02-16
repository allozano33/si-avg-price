package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.model.AverageCostDTO
import java.math.BigDecimal
import java.time.LocalDateTime

class AverageCostDTOProvider {

    companion object {
        fun get(
            id: Long? = null,
            createdAt: LocalDateTime? = LocalDateTime.now(),
            updatedAt: LocalDateTime? = LocalDateTime.now()
        ) = AverageCostDTO(
            id = id,
            sku = "1234",
            cnpj = "1234",
            stock = BigDecimal.TEN,
            averagePrice = BigDecimal.TEN,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}