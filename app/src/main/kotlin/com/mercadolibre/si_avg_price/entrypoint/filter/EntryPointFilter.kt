package com.mercadolibre.si_avg_price.entrypoint.filter

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolibre.si_avg_price.gateway.DatadogGateway
import com.mercadolibre.si_avg_price.model.datadog.MetricName
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebInputException

@Component
class EntryPointFilter(
    private val objectMapper: ObjectMapper,
    private val datadogGateway: DatadogGateway
) {

    private val log: Logger = LoggerFactory.getLogger(this.javaClass)

    fun <T> readBody(body: String, type: Class<out T>): T {

        return try {
            objectMapper.readValue(body, type)
        } catch (ex: JsonProcessingException) {
            log.error("error during body parsing", ex)

            datadogGateway.incrementMetric(
                MetricName.ERROR_PARSING.id,
                mapOf(Pair("error_parsing_class", type.toString()))
            )

            throw ServerWebInputException("error during body parsing", null, ex)
        }
    }

    fun <T> readMessage(body: String, type: Class<out T>): T {

        return try {
            objectMapper.readValue(extractMessage(body), type)
        } catch (ex: JsonProcessingException) {
            log.error("error during body parsing", ex)

            datadogGateway.incrementMetric(
                MetricName.ERROR_PARSING.id,
                mapOf(Pair("error_parsing_class", type.toString()))
            )

            throw ServerWebInputException("error during body parsing", null, ex)
        }
    }

    private fun extractMessage(message: String): String {
        val jsonTree: JsonNode = objectMapper.readTree(message)

        return jsonTree.get("msg").toString()
    }
}
