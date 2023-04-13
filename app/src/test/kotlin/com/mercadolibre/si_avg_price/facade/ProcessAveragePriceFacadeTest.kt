package com.mercadolibre.si_avg_price.facade

import com.mercadolibre.si_avg_price.exception.BusinessException
import com.mercadolibre.si_avg_price.gateway.database.AverageCostDataBase
import com.mercadolibre.si_avg_price.gateway.metric.DatadogGateway
import com.mercadolibre.si_avg_price.model.AveragePriceError.DONT_HAVE_AVERAGE_COST
import com.mercadolibre.si_avg_price.model.AveragePriceError.DONT_HAVE_IN_BASE
import com.mercadolibre.si_avg_price.model.AveragePriceError.DONT_HAVE_STOCK
import com.mercadolibre.si_avg_price.provider.AverageCostDTOProvider
import com.mercadolibre.si_avg_price.provider.AveragePriceProcessProvider
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(SpringExtension::class)
internal class ProcessAveragePriceFacadeTest {

    private lateinit var processAveragePriceFacade: ProcessAveragePriceFacade

    @MockK
    private lateinit var averageCostDataBase: AverageCostDataBase

    @MockK
    private lateinit var datadogGateway: DatadogGateway


    @BeforeEach
    fun setUp() {
        clearAllMocks()
        processAveragePriceFacade = ProcessAveragePriceFacade(
            averageCostDataBase,
            datadogGateway
        )
    }

    @Test
    fun `given process - should call facade process`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now().plusDays(1))
            val averageCostDTO = AverageCostDTOProvider.get(id = 213)

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns averageCostDTO

            coEvery {
                averageCostDataBase.saveAndUpdate(averagePriceProcess, averageCostDTO)
            } returns averageCostDTO

            coEvery {
                datadogGateway.incrementMetric(
                    "sap_average_cost",
                    mapOf("sku" to averagePriceProcess.sku, "cnpj" to averagePriceProcess.cnpj)
                )
            } just runs

            val actionProcessed = processAveragePriceFacade.execute(averagePriceProcess)

            assertEquals(averagePriceProcess.averagePrice, actionProcessed.averageCost)
            assertEquals(averagePriceProcess.sku, actionProcessed.sku)
        }
    }

    @Test
    fun `given process dont have entity - should call facade process`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now())
            val averageCostDTO = AverageCostDTOProvider.get()

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns null

            coEvery {
                averageCostDataBase.save(averagePriceProcess)
            } returns averageCostDTO

            coEvery {
                datadogGateway.incrementMetric(
                    "sap_average_cost",
                    mapOf("sku" to averagePriceProcess.sku, "cnpj" to averagePriceProcess.cnpj)
                )
            } just runs

            val actionProcessed = processAveragePriceFacade.execute(averagePriceProcess)

            assertEquals(averagePriceProcess.averagePrice, actionProcessed.averageCost)
            assertEquals(averagePriceProcess.sku, actionProcessed.sku)
        }

    }

    @Test
    fun `given process dto dont valid - should call facade process`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now())
            val averageCostDTO = AverageCostDTOProvider.get(id = 0)

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns averageCostDTO

            coEvery {
                averageCostDataBase.save(averagePriceProcess)
            } returns averageCostDTO

            coEvery {
                datadogGateway.incrementMetric(
                    "sap_average_cost",
                    mapOf("sku" to averagePriceProcess.sku, "cnpj" to averagePriceProcess.cnpj)
                )
            } just runs

            val actionProcessed = processAveragePriceFacade.execute(averagePriceProcess)

            assertEquals(averagePriceProcess.averagePrice, actionProcessed.averageCost)
            assertEquals(averagePriceProcess.sku, actionProcessed.sku)
        }

    }

    @Test
    fun `given process - should call facade get`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now())
            val averageCostDTO = AverageCostDTOProvider.get()

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns averageCostDTO
            coEvery {
                datadogGateway.gauge(
                    "average_price",
                    averageCostDTO.averagePrice.toLong(),
                    mapOf("cnpj" to averageCostDTO.cnpj)
                )
            } just runs

            val actionProcessed =
                processAveragePriceFacade.get(averagePriceProcess.cnpj, averagePriceProcess.sku)

            assertEquals(averagePriceProcess.averagePrice, actionProcessed?.averagePrice)
            assertEquals(averagePriceProcess.sku, actionProcessed?.sku)
        }
    }

    @Test
    fun `given process - should call facade get all`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now())
            val averageCostDTO = AverageCostDTOProvider.get()

            coEvery {
                averageCostDataBase.findAll()
            } returns listOf(averageCostDTO)

            val actionProcessed =
                processAveragePriceFacade.getAll()

            assertEquals(averagePriceProcess.averagePrice, actionProcessed[0].averagePrice)
            assertEquals(averagePriceProcess.sku, actionProcessed[0].sku)
        }
    }

    @Test
    fun `given process - should call facade get and dont have average cost`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now())
            val averageCostDTO = AverageCostDTOProvider.get(averagePrice = BigDecimal.ZERO)

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns averageCostDTO
            coEvery {
                datadogGateway.incrementMetric(
                    "average_price_error",
                    mapOf(
                        "sku" to averagePriceProcess.sku,
                        "cnpj" to averagePriceProcess.cnpj,
                        "status" to DONT_HAVE_AVERAGE_COST.name
                    )
                )
            } just runs

            assertThrows<BusinessException> {
                runBlocking {
                    processAveragePriceFacade.get(
                        averagePriceProcess.cnpj,
                        averagePriceProcess.sku
                    )
                }
            }
        }
    }

    @Test
    fun `given process - should call facade get and dont have stock`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now())
            val averageCostDTO = AverageCostDTOProvider.get(stock = BigDecimal.ZERO)

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns averageCostDTO
            coEvery {
                datadogGateway.incrementMetric(
                    "average_price_error",
                    mapOf(
                        "sku" to averagePriceProcess.sku,
                        "cnpj" to averagePriceProcess.cnpj,
                        "status" to DONT_HAVE_STOCK.name
                    )
                )
            } just runs

            assertThrows<BusinessException> {
                runBlocking {
                    processAveragePriceFacade.get(
                        averagePriceProcess.cnpj,
                        averagePriceProcess.sku
                    )
                }
            }
        }
    }

    @Test
    fun `given process - should call facade get and dont have in base`() {

        runBlocking {

            val averagePriceProcess =
                AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.now())

            coEvery {
                averageCostDataBase.findOneBySkuAndCnpj(
                    averagePriceProcess.sku,
                    averagePriceProcess.cnpj
                )
            } returns null
            coEvery {
                datadogGateway.incrementMetric(
                    "average_price_error",
                    mapOf(
                        "sku" to averagePriceProcess.sku,
                        "cnpj" to averagePriceProcess.cnpj,
                        "status" to DONT_HAVE_IN_BASE.name
                    )
                )
            } just runs

            assertThrows<BusinessException> {
                runBlocking {
                    processAveragePriceFacade.get(
                        averagePriceProcess.cnpj,
                        averagePriceProcess.sku
                    )
                }
            }
        }
    }
}