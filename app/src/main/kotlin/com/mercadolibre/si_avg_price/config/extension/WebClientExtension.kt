package com.mercadolibre.si_avg_price.config.extension

import com.mercadolibre.si_avg_price.exception.IntegrationServerErrorException
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.util.retry.Retry
import reactor.util.retry.RetryBackoffSpec
import java.time.Duration

private const val DEFAULT_BACKOFF_MILLIS = 100L

object RetryStrategy {
    fun retryBackoff(
        retries: Long = 3,
        backoffInterval: Duration? = Duration.ofMillis(DEFAULT_BACKOFF_MILLIS)
    ): Retry {
        return RetryBackoffSpec
            .backoff(retries, backoffInterval)
            .filter { it is IntegrationServerErrorException }
            .onRetryExhaustedThrow { _, u -> u.failure() }
    }
}

object ErrorHandler {
    fun resolveCode(
        it: Throwable
    ): Int {
        return if (it is WebClientResponseException) {
            it.rawStatusCode
        } else {
            HttpStatus.INTERNAL_SERVER_ERROR.value()
        }
    }
}