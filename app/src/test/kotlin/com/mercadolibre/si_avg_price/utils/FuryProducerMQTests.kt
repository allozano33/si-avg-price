package com.mercadolibre.si_avg_price.utils

import com.mercadolibre.json.JsonUtils
import com.mercadolibre.mqclient.entity.BulkResponse
import com.mercadolibre.mqclient.entity.Filters
import com.mercadolibre.mqclient.entity.Message
import com.mercadolibre.mqclient.producer.Producer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.ZonedDateTime
import java.util.concurrent.CompletableFuture

class FuryProducerMQTests : Producer {

    private val topic = mutableListOf<Any?>()
    private val filtersContent = mutableListOf<Any?>()
    private val deliveryDates = mutableListOf<ZonedDateTime>()

    fun clearTopic() = topic.clear()

    fun clearFilters() = filtersContent.clear()

    fun getMessages(index: Int) = topic[index]

    fun getFilters(index: Int) = filtersContent[index]

    fun getFirstDeliveryDate() = deliveryDates.firstOrNull()

    private fun sendTopic(message: Any?) {
        topic.add(message)
    }

    private fun sendFilters(filters: Filters) {
        filtersContent.add(filters)
    }

    override fun send(message: Any): ByteArray {
        this.sendTopic(message)

        return assembleBody(body = message)
            .also(::logRequest)
    }

    override fun send(message: Any, filters: Filters): ByteArray {
        this.sendTopic(message)
        this.sendFilters(filters)

        return assembleBody(body = message, filters = filters)
            .also(::logRequest)
    }

    override fun send(message: Any, filters: Filters?, deliveryDate: ZonedDateTime?): ByteArray {
        this.sendTopic(message)
        filters?.let { this.sendFilters(filters) }
        deliveryDate?.let { deliveryDates.add(it) }

        return assembleBody(body = message, filters = filters, deliveryDate = deliveryDate)
            .also(::logRequest)
    }

    override fun send(message: Any, deliveryDate: ZonedDateTime?): ByteArray {
        this.sendTopic(message)
        deliveryDate?.let { deliveryDates.add(it) }

        return assembleBody(body = message, deliveryDate = deliveryDate)
            .also(::logRequest)
    }

    override fun sendAsync(message: Any): CompletableFuture<Any> {
        this.sendTopic(message)

        return assembleBody(body = message)
            .also(::logRequest)
            .let { CompletableFuture.completedFuture(message) }
    }

    override fun sendAsync(message: Any, filters: Filters?): CompletableFuture<Any> {
        this.sendTopic(message)

        return assembleBody(body = message, filters = filters)
            .also(::logRequest)
            .let { CompletableFuture.completedFuture(message) }
    }

    override fun sendAsync(message: Any, deliveryDate: ZonedDateTime?): CompletableFuture<Any> {
        this.sendTopic(message)
        deliveryDate?.let { deliveryDates.add(it) }

        return assembleBody(body = message, deliveryDate = deliveryDate)
            .also(::logRequest)
            .let { CompletableFuture.completedFuture(message) }
    }

    override fun sendAsync(
        message: Any,
        filters: Filters?,
        deliveryDate: ZonedDateTime?
    ): CompletableFuture<Any> {
        this.sendTopic(message)
        deliveryDate?.let { deliveryDates.add(it) }

        return assembleBody(body = message, filters = filters, deliveryDate = deliveryDate)
            .also(::logRequest)
            .let { CompletableFuture.completedFuture(message) }
    }

    override fun bulkSend(messages: MutableList<Message>?): BulkResponse? {
        messages?.forEach { topic.add(it.msg) }
        return null
    }

    override fun bulkSendAsync(messages: MutableList<Message>?): CompletableFuture<BulkResponse>? {
        return null
    }

    override fun clientTimeout(): Long {
        return 1L
    }

    private fun assembleBody(
        body: Any,
        filters: Filters? = null,
        deliveryDate: ZonedDateTime? = null
    ): ByteArray {
        val payload = mutableMapOf("msg" to body)

        if (filters != null) {
            payload["filters"] = filters
        }

        if (deliveryDate != null) {
            payload["delivery_time"] = Producer.convertToSeconds(deliveryDate)
        }

        return JsonUtils.INSTANCE.toJsonString(payload).toByteArray()
    }

    private fun logRequest(json: ByteArray) =
        log.info("Producing BigQueue message: ${json.decodeToString()}")

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

}