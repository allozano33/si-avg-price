package com.mercadolibre.si_avg_price.entrypoint.handler

import com.newrelic.api.agent.NewRelic
import com.newrelic.api.agent.Trace
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component

@Component
class NewRelicErrorHandler {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Trace(async = true)
    fun handle(request: ServerHttpRequest, error: Throwable, additionalInfo: Map<String, String> = emptyMap()) {

        log.error(error.message, error)

        val queryParams = request.queryParams

        NewRelic.noticeError(error,
            additionalInfo.plus(Pair("query_params", queryParams.toString()))
                .plus(Pair("error_message", error.message)))
    }
}