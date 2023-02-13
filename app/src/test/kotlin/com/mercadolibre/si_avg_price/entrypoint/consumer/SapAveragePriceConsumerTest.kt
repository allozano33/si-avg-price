package com.mercadolibre.si_avg_price.entrypoint.consumer

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolibre.lockclient.Lock
import com.mercadolibre.si_avg_price.config.IntegrationTest
import com.mercadolibre.si_avg_price.entrypoint.filter.EntryPointFilter
import com.mercadolibre.si_avg_price.entrypoint.handler.NewRelicErrorHandler
import com.mercadolibre.si_avg_price.entrypoint.resource.BQMessage
import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input.SapInput
import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.output.SapOutput
import com.mercadolibre.si_avg_price.facade.ProcessAveragePriceFacade
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import com.mercadolibre.si_avg_price.provider.SapInputProvider
import com.mercadolibre.si_avg_price.provider.SapOutputProvider
import com.mercadolibre.si_avg_price.service.LockService
import com.mercadolibre.si_avg_price.utils.loadJsonAsString
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean

@WebFluxTest(SapAveragePriceConsumer::class)
internal class SapAveragePriceConsumerTest : IntegrationTest() {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var entryPointFilter: EntryPointFilter

    @MockkBean
    private lateinit var processAveragePriceFacade: ProcessAveragePriceFacade

    @MockkBean
    private lateinit var lockService: LockService

    @MockBean
    lateinit var newRelicErrorHandler: NewRelicErrorHandler

    @MockK
    private lateinit var lock: Lock


    @Test
    fun `given a input - should send and return sap output`() {

        val input = SapInputProvider.get()
        val msg = objectMapper.writeValueAsString(BQMessage(input))
        val sapOutput = SapOutputProvider.get()

        coEvery {
            entryPointFilter.readMessage(msg, SapInput::class.java)
        } returns input

        coEvery { lockService.lock(any()) } returns lock
        coJustRun { lockService.unlock(any()) }

        processFacadeMock(input.toDomain(), sapOutput)

        webClientPost(BQMessage(input))
            .expectStatus().isOk
            .expectBody()
            .json(loadJsonAsString("src/test/resources/__files/entrypoint/consumer/consumer_will_be_process.json"))
    }

    private fun webClientPost(input: BQMessage<SapInput>) =
        webTestClient.post()
            .uri("/consumer/sap/average-price/process")
            .bodyValue(input)
            .exchange()

    private fun processFacadeMock(averagePriceProcess: AveragePriceProcess, sapOutput: SapOutput) =
        coEvery {
            processAveragePriceFacade.execute(averagePriceProcess)
        } returns sapOutput

}
