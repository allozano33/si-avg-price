package com.mercadolibre.si_avg_price.facade

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.output.SapOutput
import com.mercadolibre.si_avg_price.gateway.database.AverageCostDataBase
import com.mercadolibre.si_avg_price.model.AverageCostDTO
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProcessAveragePriceFacade(
    private val averageCostDataBase: AverageCostDataBase
) {

    suspend fun execute(averagePriceProcess: AveragePriceProcess): SapOutput =
        averageCostDataBase.findOneBySkuAndCnpj(averagePriceProcess.sku, averagePriceProcess.cnpj)
            .let {
                averageCostDataBase.save(averagePriceProcess)
            }.let { SapOutput(averagePriceProcess.sku, averagePriceProcess.averagePrice) }

    suspend fun get(cnpj: String, sku: String): AverageCostDTO? =
        averageCostDataBase.findOneBySkuAndCnpj(sku, cnpj)
            .let {
                if (it != null && it.stock > BigDecimal.ZERO) {
                    return it
                }
                return null
            }

}