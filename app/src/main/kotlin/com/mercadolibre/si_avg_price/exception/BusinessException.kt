package com.mercadolibre.si_avg_price.exception

open class BusinessException(
    message: String,
    open val code: Int,
    val status: String = "ERROR",
    val shouldRetry: Boolean = true
) : Exception(message)