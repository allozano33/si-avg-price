package com.mercadolibre.si_avg_price.model

import java.math.BigDecimal

data class AverageCostDTO(
    val sku: String,
    val cnpj: String,
    val stock: BigDecimal,
    val averagePrice: BigDecimal
)