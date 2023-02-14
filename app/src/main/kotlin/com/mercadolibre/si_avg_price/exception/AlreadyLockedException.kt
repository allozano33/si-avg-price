package com.mercadolibre.si_avg_price.exception

class AlreadyLockedException(
    val resource: String,
    cause: Throwable,
    override val message: String
): Exception(message, cause)