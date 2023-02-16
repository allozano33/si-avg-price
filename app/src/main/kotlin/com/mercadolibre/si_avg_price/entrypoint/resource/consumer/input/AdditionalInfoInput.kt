package com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal


data class AdditionalInfoInput(
    @JsonProperty("CNPJ")
    val cnpj: String? = null,
    @JsonProperty("STOCK")
    val stock: BigDecimal? = null,
    @JsonProperty("COSTO")
    val costo: BigDecimal? = null,
)
