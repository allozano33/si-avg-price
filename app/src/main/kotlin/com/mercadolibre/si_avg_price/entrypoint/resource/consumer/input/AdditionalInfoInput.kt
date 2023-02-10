package com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input

import java.math.BigDecimal
import java.time.LocalDateTime


data class AdditionalInfoInput(
    val CNPJ: String,
    val STOCK: BigDecimal,
    val COSTO: BigDecimal,
    val E_DATLO: LocalDateTime
)
