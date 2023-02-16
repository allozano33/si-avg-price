package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input.SapInput
import java.time.LocalDateTime

class SapInputProvider {

    companion object {
        fun get() = SapInput(
            entity = "CostoyDisponibilidad",
            id = "1234",
            action = "update",
            date = LocalDateTime.of(2023,1,1,0,0),
            sapMessageId = "42010a5502281edda9c91f2c1066452f",
            additionalInfo = AdditionalInfoInputProvider.get()
        )

        fun getDontAdditionalInfo() = SapInput(
            entity = "CostoyDisponibilidad",
            id = "1234",
            action = "update",
            date = LocalDateTime.of(2023,1,1,0,0),
            sapMessageId = "42010a5502281edda9c91f2c1066452f",
            additionalInfo = AdditionalInfoInputProvider.getDontValid()
        )
    }
}