package com.mercadolibre.si_avg_price.exception

class AlreadyExecutedException(
    override val message: String,
    override val code: Int = 10098
) : DefaultErrorException(message, code) {

    companion object {
        const val ALREADY_EXECUTED_MESSAGE =
            "document id already been executed [document_id:%d][document_type:%s]"
    }
}