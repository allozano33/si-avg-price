package com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input

import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import java.time.LocalDateTime


data class SapInput(
    val entity: String,
    val id: String,
    val action: String,
    val date: LocalDateTime,
    val sap_message_id: String,
    val additional_info: AdditionalInfoInput

) {


    fun toDomain() =
        AveragePriceProcess(
            sku = id,
            cnpj = additional_info.CNPJ,
            stock = additional_info.STOCK,
            averagePrice = additional_info.COSTO
        )
}
