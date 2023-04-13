package com.mercadolibre.si_avg_price.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class AveragePriceProcess(
    val sku: String,
    val cnpj: String,
    val stock: BigDecimal,
    val averagePrice: BigDecimal,
    val dateUpdate: LocalDateTime
) {
    fun isNew(dateUpdateDb: LocalDateTime): Boolean {
        return dateUpdateDb.isBefore(dateUpdate)
    }
}