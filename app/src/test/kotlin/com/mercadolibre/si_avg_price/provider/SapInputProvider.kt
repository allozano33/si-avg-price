package com.mercadolibre.si_avg_price.provider

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input.SapInput
import java.time.LocalDateTime

class SapInputProvider {

    companion object {
        fun get() = SapInput(
            entity = "sap",
            id = "1234",
            action = "update",
            date = LocalDateTime.now(),
            sap_message_id = "12314123",
            additional_info = AdditionalInfoInputProvider.get()
        )
    }
}