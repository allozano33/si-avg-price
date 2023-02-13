package com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal


data class AdditionalInfoInput(
    @JsonProperty("CNPJ")
    val cnpj: String,
    @JsonProperty("STOCK")
    val stock: BigDecimal,
    @JsonProperty("COSTO")
    val costo: BigDecimal,
)
