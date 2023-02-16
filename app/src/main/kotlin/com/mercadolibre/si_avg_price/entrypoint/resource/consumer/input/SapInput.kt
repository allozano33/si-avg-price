package com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input

import com.fasterxml.jackson.annotation.JsonProperty
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import java.time.LocalDateTime


data class SapInput(
    @JsonProperty("entity")
    val entity: String,
    @JsonProperty("id")
    val id: String,
    @JsonProperty("action")
    val action: String,
    @JsonProperty("date")
    val date: LocalDateTime?,
    @JsonProperty("sap_message_id")
    val sapMessageId: String?,
    @JsonProperty("additional_info")
    val additionalInfo: AdditionalInfoInput

) {


    fun toDomain() =
        AveragePriceProcess(
            sku = id,
            cnpj = additionalInfo.cnpj!!,
            stock = additionalInfo.stock!!,
            averagePrice = additionalInfo.costo!!
        )

    fun isValid() =
        additionalInfo.cnpj != null && additionalInfo.costo != null && additionalInfo.stock != null
}
