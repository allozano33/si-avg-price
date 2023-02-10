package com.mercadolibre.si_avg_price.exception

class ImplementationNotExistException(
    override val message: String,
    override val code: Int = 10098
) : DefaultErrorException(message, code) {

    companion object {
        const val IMPLEMENTATION_NOT_EXIST_MESSAGE =
            "implementation for %s not exist. Possible implementations are %s"
    }
}