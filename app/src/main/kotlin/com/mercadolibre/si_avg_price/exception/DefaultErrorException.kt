package com.mercadolibre.si_avg_price.exception

open class DefaultErrorException(
    message: String,
    open val code: Int,
    open val shouldRetry: Boolean = true
) : Exception(message)