package com.mercadolibre.si_avg_price.gateway.database

import com.mercadolibre.si_avg_price.model.AverageCostDTO
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import com.mercadolibre.si_avg_price.repository.AveragePriceRepository
import com.mercadolibre.si_avg_price.resourse.database.AveragePriceDB
import kotlinx.coroutines.flow.toList
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component


@Component
class AverageCostGatewayDatabase(
    private val averagePriceRepository: AveragePriceRepository
) : AverageCostDataBase {

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }
    override suspend fun findOneBySkuAndCnpj(sku: String, cnpj: String): AverageCostDTO? {
        log.warn( "call base  $sku $cnpj")
        val findOneBySkuAndCnpj = averagePriceRepository.findOneBySkuAndCnpj(sku, cnpj)
        log.warn("return base  $findOneBySkuAndCnpj")
        return findOneBySkuAndCnpj?.toDomain()
    }

    override suspend fun save(averagePriceProcess: AveragePriceProcess): AverageCostDTO {
        return averagePriceRepository.save(AveragePriceDB.of(averagePriceProcess))
            .toDomain()
    }

    override suspend fun saveAndUpdate(
        averagePriceProcess: AveragePriceProcess,
        averageDTO: AverageCostDTO
    ): AverageCostDTO {
        return averagePriceRepository.save(
            AveragePriceDB.saveAndUpdate(
                averageDTO,
                averagePriceProcess
            )
        ).toDomain()
    }

    override suspend fun findAll(): List<AverageCostDTO> {
        return averagePriceRepository.findAll().toList().map { it.toDomain() }
    }

}