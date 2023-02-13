package com.mercadolibre.si_avg_price.gateway.metric

import com.mercadolibre.metrics.MetricCollector
import com.mercadolibre.si_avg_price.gateway.DatadogGateway
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class DatadogGatewaySenderTest {

    @MockK(relaxUnitFun = true)
    lateinit var collector: MetricCollector

    private lateinit var datadogGateway: DatadogGateway

    @BeforeEach
    fun setup() {
        datadogGateway = DatadogGatewaySender(collector)
    }

    @Test
    fun `given metric data - should send successfully`() {

        val captor = slot<MetricCollector.Tags>()

        datadogGateway.incrementMetric(
            "error_parsing",
            mapOf(
                Pair("error_parsing_class", "ProcessConsumer")
            )
        )

        verify(exactly = 1) { collector.incrementCounter(eq("si_avg_price.error_parsing"), capture(captor)) }

        val tags = captor.captured.toArray()

        val expectedTags = MetricCollector.Tags()
            .add("error_parsing_class", "ProcessConsumer").toArray()

        tags.forEachIndexed { idx, value ->
            Assertions.assertEquals(expectedTags[idx], value)
        }
    }

    @Test
    fun `given metric gauge data - should send successfully`() {

        val captor = slot<MetricCollector.Tags>()

        datadogGateway.gauge(
            key = "key",
            value = 1,
            mapOf(
                Pair("key", "value")
            )
        )

        verify(exactly = 1) { collector.gauge(eq("si_avg_price.key"), eq(1), capture(captor)) }

        val tags = captor.captured.toArray()

        val expectedTags = MetricCollector.Tags()
            .add("key", "value").toArray()

        tags.forEachIndexed { idx, value ->
            Assertions.assertEquals(expectedTags[idx], value)
        }
    }
}