package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input.AdditionalInfoInput
import java.math.BigDecimal

class AdditionalInfoInputProvider {

    companion object {
        fun get() = AdditionalInfoInput(
            cnpj = "1234",
            stock = BigDecimal.TEN,
            costo = BigDecimal.TEN
        )

        fun getDontValid() = AdditionalInfoInput(
            cnpj = null,
            stock = BigDecimal.TEN,
            costo = BigDecimal.TEN
        )
    }
}