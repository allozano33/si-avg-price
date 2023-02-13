package com.mercadolibre.si_avg_price.facade

import com.mercadolibre.si_avg_price.gateway.database.AverageCostDataBase
import com.mercadolibre.si_avg_price.provider.AverageCostDTOProvider
import com.mercadolibre.si_avg_price.provider.AveragePriceProcessProvider
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
internal class ProcessAveragePriceFacadeTest {

    private lateinit var processAveragePriceFacade: ProcessAveragePriceFacade

    @MockK
    private lateinit var averageCostDataBase: AverageCostDataBase


    @BeforeEach
    fun setUp() {
        clearAllMocks()
        processAveragePriceFacade = ProcessAveragePriceFacade(
            averageCostDataBase
        )
    }

    @Test
    fun `given process - should call facade process`() {

        runBlocking {

            val averagePriceProcess = AveragePriceProcessProvider.get()
            val averageCostDTO = AverageCostDTOProvider.get()

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns averageCostDTO

            coEvery {
                averageCostDataBase.save(averagePriceProcess)
            } returns averageCostDTO


            val actionProcessed = processAveragePriceFacade.execute(averagePriceProcess)

            assertEquals(averagePriceProcess.averagePrice, actionProcessed.averageCost)
            assertEquals(averagePriceProcess.sku, actionProcessed.sku)
        }
    }

    @Test
    fun `given process - should call facade get`() {

        runBlocking {

            val averagePriceProcess = AveragePriceProcessProvider.get()
            val averageCostDTO = AverageCostDTOProvider.get()

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns averageCostDTO


            val actionProcessed =
                processAveragePriceFacade.get(averagePriceProcess.cnpj, averagePriceProcess.sku)

            assertEquals(averagePriceProcess.averagePrice, actionProcessed?.averagePrice)
            assertEquals(averagePriceProcess.sku, actionProcessed?.sku)
        }
    }


}