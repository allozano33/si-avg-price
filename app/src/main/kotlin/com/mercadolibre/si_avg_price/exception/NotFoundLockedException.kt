package com.mercadolibre.si_avg_price.exception

class NotFoundLockedException(
    cause: Throwable,
    override val message: String
) : Exception(message, cause)