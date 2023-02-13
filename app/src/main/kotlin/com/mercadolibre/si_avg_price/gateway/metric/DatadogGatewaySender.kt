package com.mercadolibre.si_avg_price.gateway.metric

import com.mercadolibre.metrics.MetricCollector
import com.mercadolibre.si_avg_price.gateway.DatadogGateway
import org.springframework.stereotype.Component

@Component
class DatadogGatewaySender(
    private val collector: MetricCollector
) : DatadogGateway {

    override fun incrementMetric(name: String, extraTags: Map<String, String>?) {
        val tags = MetricCollector.Tags()
        extraTags?.map { tags.add(it.key, it.value) }

        collector.incrementCounter("si_avg_price.$name", tags)
    }

    override fun gauge(key: String, value: Long, extraTags: Map<String, String>?) {
        val tags = MetricCollector.Tags()
        extraTags?.forEach { tag -> tags.add(tag.key, tag.value) }

        collector.gauge("si_avg_price.$key", value, tags)
    }
}