package com.mercadolibre.si_avg_price.entrypoint.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolibre.si_avg_price.config.ObjectMapperConfig
import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input.SapInput
import com.mercadolibre.si_avg_price.gateway.metric.DatadogGateway
import com.mercadolibre.si_avg_price.model.datadog.MetricName
import com.mercadolibre.si_avg_price.provider.PayloadProvider
import com.mercadolibre.si_avg_price.provider.SapInputProvider
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearAllMocks
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.web.server.ServerWebInputException

@ExtendWith(SpringExtension::class)
@Import(value = [ObjectMapperConfig::class, Jackson2ObjectMapperBuilder::class])
internal class EntryPointFilterTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var datadogGateway: DatadogGateway

    private lateinit var entryPointFilter: EntryPointFilter

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        entryPointFilter = EntryPointFilter(objectMapper, datadogGateway)
    }

    @Test
    fun `given a average price from queue - should parse successfully`() {
        val body =
            PayloadProvider("/__files/entrypoint/filter/process_input.json")
                .provideAsString()

        val sapInput = SapInputProvider.get()

        val readBody = entryPointFilter.readMessage(body, SapInput::class.java)

        assertEquals(sapInput, readBody)
    }

    @Test
    fun `given a average price queue with wrong format - should throw ServerWebInputException`() {
        val body =
            PayloadProvider("/__files/entrypoint/filter/process_input_wrong_format.json")
                .provideAsString()

        every {
            datadogGateway.incrementMetric(
                MetricName.ERROR_PARSING.id,
                mapOf(Pair("error_parsing_class", SapInput::class.java.toString()))
            )
        } returns Unit

        val exception = Assertions.assertThrows(ServerWebInputException::class.java) {
            entryPointFilter.readMessage(body, SapInput::class.java)
        }

        assertEquals("error during body parsing", exception.reason)
    }

    @Test
    fun `given a average price from controller - should parse successfully`() {
        val body =
            PayloadProvider("/__files/entrypoint/filter/process_input_not_queue.json")
                .provideAsString()

        val sapInput = SapInputProvider.get()

        val readBody = entryPointFilter.readBody(body, SapInput::class.java)

        assertEquals(sapInput, readBody)
    }

    @Test
    fun `given a average price from controller with wrong format - should throw ServerWebInputException`() {

        val body =
            PayloadProvider("/__files/entrypoint/filter/process_input_wrong_format.json")
                .provideAsString()

        every {
            datadogGateway.incrementMetric(
                MetricName.ERROR_PARSING.id,
                mapOf(Pair("error_parsing_class", SapInput::class.java.toString()))
            )
        } returns Unit

        val exception = Assertions.assertThrows(ServerWebInputException::class.java) {
            entryPointFilter.readBody(body, SapInput::class.java)
        }

        assertEquals("error during body parsing", exception.reason)
    }
}
