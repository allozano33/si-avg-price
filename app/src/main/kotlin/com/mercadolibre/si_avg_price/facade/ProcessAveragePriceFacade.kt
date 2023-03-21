package com.mercadolibre.si_avg_price.facade

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.output.SapOutput
import com.mercadolibre.si_avg_price.exception.BusinessException
import com.mercadolibre.si_avg_price.gateway.database.AverageCostDataBase
import com.mercadolibre.si_avg_price.gateway.metric.DatadogGateway
import com.mercadolibre.si_avg_price.model.AverageCostDTO
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProcessAveragePriceFacade(
    private val averageCostDataBase: AverageCostDataBase,
    private val datadogGateway: DatadogGateway
) {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    suspend fun execute(averagePriceProcess: AveragePriceProcess): SapOutput =
        averageCostDataBase.findOneBySkuAndCnpj(averagePriceProcess.sku, averagePriceProcess.cnpj)
            .let { averageDTO ->
                when {
                    averageDTO != null && averageDTO.isValid() -> averageCostDataBase.saveAndUpdate(
                        averagePriceProcess,
                        averageDTO
                    )

                    else -> averageCostDataBase.save(averagePriceProcess)

                }
               log.info("average cost save $averagePriceProcess")
                datadogGateway.incrementMetric(
                    "sap_average_cost", mapOf(
                        Pair("sku", averagePriceProcess.sku),
                        Pair("cnpj", averagePriceProcess.cnpj)
                    )
                )
            }.let { SapOutput(averagePriceProcess.sku, averagePriceProcess.averagePrice) }

    suspend fun get(cnpj: String, sku: String): AverageCostDTO? =
        averageCostDataBase.findOneBySkuAndCnpj(sku, cnpj)
            .let {
                log.warn("find $it")
                if (it != null && it.stock > BigDecimal.ZERO) {
                    datadogGateway.gauge(
                        key = "average_price",
                        value = it.averagePrice.longValueExact(),
                        extraTags = mapOf(
                            Pair("cnpj", it.cnpj)
                        )
                    )
                    log.warn("return $it")
                    return it
                }
                log.warn("exeption $it")
                throw BusinessException("Dont Have average price", 10373)
            }

    suspend fun getAll(): List<AverageCostDTO> =
        averageCostDataBase.findAll()
}