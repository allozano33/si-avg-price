package com.mercadolibre.si_avg_price.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class AverageCostDTO(
    val id: Long? = null,
    val sku: String,
    val cnpj: String,
    val stock: BigDecimal,
    val averagePrice: BigDecimal,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun isValid() = id != null && id > 0L
}