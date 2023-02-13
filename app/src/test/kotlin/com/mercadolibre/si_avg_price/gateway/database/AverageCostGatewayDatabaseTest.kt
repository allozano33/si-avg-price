package com.mercadolibre.si_avg_price.gateway.database

import com.mercadolibre.si_avg_price.config.DatabaseTest
import com.mercadolibre.si_avg_price.provider.AveragePriceProcessProvider
import com.mercadolibre.si_avg_price.repository.AveragePriceRepository
import com.mercadolibre.si_avg_price.resourse.database.AveragePriceDB
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

internal class AverageCostGatewayDatabaseTest : DatabaseTest() {

    @Autowired
    private lateinit var averagePriceRepository: AveragePriceRepository


    private lateinit var averageCostGatewayDatabase: AverageCostGatewayDatabase

    @BeforeEach
    fun setUp() {
        averageCostGatewayDatabase =
            AverageCostGatewayDatabase(
                averagePriceRepository
            )

        runBlocking {
            averagePriceRepository.deleteAll()
        }
    }

    @Test
    fun `given a cnpj and sku - should return a average cost from db`() {

        runBlocking {

            val all: List<AveragePriceDB> = averagePriceRepository.findAll().toList()
            assertTrue(all.isEmpty())

            val averagePrice = AveragePriceProcessProvider.get()

            averageCostGatewayDatabase.save(averagePrice)

            val averagePriceDB =
                averageCostGatewayDatabase.findOneBySkuAndCnpj(averagePrice.sku, averagePrice.cnpj)

            assertEquals(averagePrice.sku, averagePriceDB?.sku)
            assertEquals(averagePrice.averagePrice, averagePriceDB?.averagePrice)
            assertEquals(averagePrice.cnpj, averagePriceDB?.cnpj)
            assertEquals(averagePrice.stock, averagePriceDB?.stock)
        }
    }


}