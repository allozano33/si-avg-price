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
            sku = removeZero(id),
            cnpj = additionalInfo.cnpj!!,
            stock = additionalInfo.stock!!,
            averagePrice = additionalInfo.costo!!,
            dateUpdate = date!!
        )

    private fun removeZero(sku: String): String {
        var auxSku = sku
        while (auxSku.toCharArray()[0].toString() == "0") {
            auxSku = auxSku.drop(1)
        }
        return auxSku
    }

    fun isValid() =
        additionalInfo.cnpj != null && additionalInfo.costo != null && additionalInfo.stock != null
}
