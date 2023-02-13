package com.mercadolibre.si_avg_price.entrypoint.handler

import com.mercadolibre.si_avg_price.entrypoint.resource.handler.DefaultErrorOutput
import com.mercadolibre.si_avg_price.exception.AlreadyExecutedException
import com.mercadolibre.si_avg_price.exception.BusinessException
import com.mercadolibre.si_avg_price.exception.ImplementationNotExistException
import com.mercadolibre.si_avg_price.exception.IntegrationClientErrorException
import com.mercadolibre.si_avg_price.exception.IntegrationServerErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.server.ServerWebInputException

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(IntegrationClientErrorException::class)
    fun handleIntegrationClientErrorException(
        exception: IntegrationClientErrorException
    ): ResponseEntity<DefaultErrorOutput> {
        logger.error(exception.message, exception)

        return ResponseEntity
            .unprocessableEntity()
            .body(DefaultErrorOutput(message = exception.message, errorCode = exception.code))
    }

    @ResponseBody
    @ExceptionHandler(IntegrationServerErrorException::class)
    fun handleIntegrationServerErrorException(
        exception: IntegrationServerErrorException
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error(exception.message, exception)

        return ResponseEntity
            .internalServerError()
            .body(DefaultErrorOutput(message = exception.message, errorCode = exception.code))
    }

    @ResponseBody
    @ExceptionHandler(ServerWebInputException::class)
    fun handleServerWebInputException(
        exception: ServerWebInputException
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error(exception.message, exception)

        return ResponseEntity
            .unprocessableEntity()
            .body(DefaultErrorOutput(message = exception.message, errorCode = DEFAULT_ERROR_CODE))
    }

    @ResponseBody
    @ExceptionHandler(ImplementationNotExistException::class)
    fun handleImplementationNotExistException(
        exception: ImplementationNotExistException
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error(exception.message, exception)

        return ResponseEntity
            .unprocessableEntity()
            .body(DefaultErrorOutput(message = exception.message, errorCode = DEFAULT_ERROR_CODE))
    }

    @ResponseBody
    @ExceptionHandler(AlreadyExecutedException::class)
    fun handleAlreadyExecutedException(
        exception: AlreadyExecutedException
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error(exception.message, exception)

        return ResponseEntity
            .ok()
            .body(DefaultErrorOutput(message = exception.message, errorCode = DEFAULT_ERROR_CODE))
    }


    @ResponseBody
    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(
        exception: BusinessException
    ): ResponseEntity<DefaultErrorOutput> {
        logger.error(exception.message, exception)

        if (exception.shouldRetry) {
            return ResponseEntity
                .badRequest()
                .body(
                    DefaultErrorOutput(
                        message = exception.message ?: DEFAULT_MESSAGE_EXCEPTION,
                        errorCode = exception.code
                    )
                )
        } else {
            return ResponseEntity
                .ok()
                .body(
                    DefaultErrorOutput(
                        message = exception.message ?: DEFAULT_MESSAGE_EXCEPTION,
                        errorCode = exception.code
                    )
                )
        }
    }

    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun handleException(
        exception: Exception
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error("occurred an error not expected ", exception)

        return ResponseEntity
            .internalServerError()
            .body(
                DefaultErrorOutput(
                    message = exception.message ?: DEFAULT_MESSAGE_EXCEPTION,
                    errorCode = DEFAULT_ERROR_CODE
                )
            )
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(
        exception: IllegalArgumentException
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error(exception.message, exception)

        return ResponseEntity
            .unprocessableEntity()
            .body(
                DefaultErrorOutput(
                    message = exception.message ?: "IllegalArgumentException occurred",
                    errorCode = DEFAULT_ERROR_CODE
                )
            )
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
        private val DEFAULT_MESSAGE_EXCEPTION = HttpStatus.INTERNAL_SERVER_ERROR.name
        private const val DEFAULT_ERROR_CODE = 10098
    }
}