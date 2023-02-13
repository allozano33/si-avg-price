package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input.AdditionalInfoInput
import java.math.BigDecimal
import java.time.LocalDateTime

class AdditionalInfoInputProvider {

    companion object {
        fun get() = AdditionalInfoInput(
            CNPJ = "1234",
            STOCK = BigDecimal.TEN,
            COSTO = BigDecimal.TEN,
            E_DATLO = LocalDateTime.now()
        )
    }
}