package com.mercadolibre.si_avg_price.controller

import com.mercadolibre.si_avg_price.config.IntegrationTest
import com.mercadolibre.si_avg_price.entrypoint.controller.AveragePriceController
import com.mercadolibre.si_avg_price.facade.ProcessAveragePriceFacade
import com.mercadolibre.si_avg_price.provider.AverageCostDTOProvider
import com.mercadolibre.si_avg_price.utils.loadJsonAsString
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest

@WebFluxTest(AveragePriceController::class)
class AveragePriceControllerTest : IntegrationTest() {

    @MockkBean
    private lateinit var processAveragePriceFacade: ProcessAveragePriceFacade


    @Test
    fun `given a cnpj and aku - should return from database successfully`() {
        val averageCostDTO = AverageCostDTOProvider.get()

        coEvery {
            processAveragePriceFacade.get(CNPJ, SKU)
        } returns averageCostDTO

        webClientGet()
            .expectStatus().isOk
            .expectBody()
            .json(loadJsonAsString("src/test/resources/__files/entrypoint/controller/average_cost.json"))
    }


    private fun webClientGet() =
        webTestClient.get()
            .uri("/average-price//$CNPJ/sku/$SKU")
            .exchange()


    companion object {
        private const val CNPJ = "123456"
        private const val SKU = "123456"
    }
}