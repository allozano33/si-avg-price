package com.mercadolibre.si_avg_price.gateway.database

import com.mercadolibre.si_avg_price.model.AverageCostDTO
import com.mercadolibre.si_avg_price.model.AveragePriceProcess

interface AverageCostDataBase {

    suspend fun findOneBySkuAndCnpj(sku: String, cnpj: String): AverageCostDTO?

    suspend fun save(averagePriceProcess: AveragePriceProcess): AverageCostDTO
    suspend fun saveAndUpdate(
        averagePriceProcess: AveragePriceProcess,
        averageDTO: AverageCostDTO
    ): AverageCostDTO

    suspend fun findAll(): List<AverageCostDTO>
}