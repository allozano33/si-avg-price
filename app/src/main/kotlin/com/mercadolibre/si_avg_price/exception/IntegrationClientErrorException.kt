package com.mercadolibre.si_avg_price.exception

class IntegrationClientErrorException(
    override val message: String,
    override val code: Int = 10098
) : DefaultErrorException(message, code)
