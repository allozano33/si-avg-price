package com.mercadolibre.si_avg_price.repository

import com.mercadolibre.si_avg_price.resource.database.AveragePriceDB
import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface AveragePriceRepository : CoroutineCrudRepository<AveragePriceDB, Long> {

    @Query("select * from average_price where sku = :sku and cnpj = :cnpj")
    suspend fun findOneBySkuAndCnpj(sku: String, cnpj: String): AveragePriceDB?

}
