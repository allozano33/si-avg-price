package com.mercadolibre.si_avg_price.model

import java.math.BigDecimal

data class AverageCostDTO(
    val id: Long? = null,
    val sku: String,
    val cnpj: String,
    val stock: BigDecimal,
    val averagePrice: BigDecimal
) {
    fun isValid() = id != null && id > 0L
}