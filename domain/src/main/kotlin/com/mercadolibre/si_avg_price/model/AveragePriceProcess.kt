package com.mercadolibre.si_avg_price.model

import java.math.BigDecimal

data class AveragePriceProcess(
    val sku: String,
    val cnpj: String,
    val stock: BigDecimal,
    val averagePrice: BigDecimal
)