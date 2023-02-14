package com.mercadolibre.si_avg_price.exception

class IntegrationServerErrorException(
    override val message: String,
    override val code: Int = 10098
) : DefaultErrorException(message, code)