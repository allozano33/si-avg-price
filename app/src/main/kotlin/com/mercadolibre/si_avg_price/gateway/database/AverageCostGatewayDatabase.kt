package com.mercadolibre.si_avg_price.gateway.database

import com.mercadolibre.si_avg_price.model.AverageCostDTO
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import com.mercadolibre.si_avg_price.repository.AveragePriceRepository
import com.mercadolibre.si_avg_price.resourse.database.AveragePriceDB
import org.springframework.stereotype.Component


@Component
class AverageCostGatewayDatabase(
    private val averagePriceRepository: AveragePriceRepository
) : AverageCostDataBase {
    override suspend fun findOneBySkuAndCnpj(sku: String, cnpj: String): AverageCostDTO? {
        return averagePriceRepository.findOneBySkuAndCnpj(sku, cnpj)?.toDomain()
    }

    override suspend fun save(averagePriceProcess: AveragePriceProcess): AverageCostDTO {
        return averagePriceRepository.save(AveragePriceDB.of(averagePriceProcess))
            .toDomain()
    }


}