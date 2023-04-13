package com.mercadolibre.si_avg_price.controller

import com.mercadolibre.si_avg_price.config.IntegrationTest
import com.mercadolibre.si_avg_price.entrypoint.controller.AveragePriceController
import com.mercadolibre.si_avg_price.facade.ProcessAveragePriceFacade
import com.mercadolibre.si_avg_price.provider.AverageCostDTOProvider
import com.mercadolibre.si_avg_price.provider.AveragePriceProcessProvider
import com.mercadolibre.si_avg_price.provider.SapOutputProvider
import com.mercadolibre.si_avg_price.utils.loadJsonAsString
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import java.math.BigDecimal
import java.time.LocalDateTime

@WebFluxTest(AveragePriceController::class)
class AveragePriceControllerTest : IntegrationTest() {

    @MockkBean
    private lateinit var processAveragePriceFacade: ProcessAveragePriceFacade


    @Test
    fun `given a cnpj and aku - should return from database successfully`() {

        val averageCostDTO =
            AverageCostDTOProvider.get(stock = BigDecimal.TEN, averagePrice = BigDecimal.TEN)

        coEvery {
            processAveragePriceFacade.get(CNPJ, SKU)
        } returns averageCostDTO

        webClientGet()
            .expectStatus().isOk
            .expectBody()
            .json(loadJsonAsString("src/test/resources/__files/entrypoint/controller/average_cost.json"))
    }

    @Test
    fun `given all average cost - should return from database successfully`() {

        val averageCostDTO =
            AverageCostDTOProvider.get(stock = BigDecimal.TEN, averagePrice = BigDecimal.TEN)

        coEvery {
            processAveragePriceFacade.getAll()
        } returns listOf(averageCostDTO)

        webClientGetAll()
            .expectStatus().isOk
            .expectBody()
            .json(loadJsonAsString("src/test/resources/__files/entrypoint/controller/average_cost_list.json"))
    }

    @Test
    fun `insert cost - should return from database successfully`() {

        val sapOutput = SapOutputProvider.get()
        val averagePriceProcess = AveragePriceProcessProvider.get(dateUpdate = LocalDateTime.of(2023,4,13,0,0,0,0,))
        coEvery {
            processAveragePriceFacade.execute(averagePriceProcess)
        } returns sapOutput

        webClientInsert()
            .expectStatus().isOk
            .expectBody()
            .json(loadJsonAsString("src/test/resources/__files/entrypoint/controller/sap_output.json"))
    }


    private fun webClientGet() =
        webTestClient.get()
            .uri("/average-price/cnpj/$CNPJ/sku/$SKU")
            .exchange()

    private fun webClientGetAll() =
        webTestClient.get()
            .uri("/average-price/listAll")
            .exchange()

    private fun webClientInsert() =
        webTestClient.post()
            .uri("/average-price/insert")
            .header("content-type", "application/json")
            .bodyValue(loadJsonAsString("src/test/resources/__files/entrypoint/controller/controller_insert_average_cost.json"))
            .exchange()


    companion object {
        private const val CNPJ = "123456"
        private const val SKU = "123456"
    }
}