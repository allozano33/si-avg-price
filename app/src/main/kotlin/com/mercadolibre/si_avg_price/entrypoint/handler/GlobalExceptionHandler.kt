package com.mercadolibre.si_avg_price.entrypoint.handler

import com.mercadolibre.si_avg_price.entrypoint.resource.handler.DefaultErrorOutput
import com.mercadolibre.si_avg_price.exception.AlreadyLockedException
import com.mercadolibre.si_avg_price.exception.BusinessException
import com.mercadolibre.si_avg_price.exception.IntegrationClientErrorException
import com.mercadolibre.si_avg_price.exception.IntegrationServerErrorException
import com.mercadolibre.si_avg_price.exception.NotFoundLockedException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
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
        exception: IntegrationClientErrorException,
        request: ServerHttpRequest
    ): ResponseEntity<DefaultErrorOutput> {
        logger.error("1 $exception.message", exception)

        return ResponseEntity
            .unprocessableEntity()
            .body(DefaultErrorOutput(message = exception.message, errorCode = exception.code))
    }

    @ResponseBody
    @ExceptionHandler(IntegrationServerErrorException::class)
    fun handleIntegrationServerErrorException(
        exception: IntegrationServerErrorException,
        request: ServerHttpRequest
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error("2 $exception.message", exception)

        return ResponseEntity
            .internalServerError()
            .body(DefaultErrorOutput(message = exception.message, errorCode = exception.code))
    }

    @ResponseBody
    @ExceptionHandler(AlreadyLockedException::class)
    fun handleAlreadyLockedException(
        exception: AlreadyLockedException,
        request: ServerHttpRequest
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error("3 $exception.message", exception)

        return ResponseEntity
            .ok()
            .body(DefaultErrorOutput(message = exception.message, errorCode = DEFAULT_ERROR_CODE))
    }

    @ResponseBody
    @ExceptionHandler(NotFoundLockedException::class)
    fun handleNotFoundLockedException(
        exception: NotFoundLockedException,
        request: ServerHttpRequest
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error("4 $exception.message", exception)

        return ResponseEntity
            .ok()
            .body(DefaultErrorOutput(message = exception.message, errorCode = DEFAULT_ERROR_CODE))
    }

    @ResponseBody
    @ExceptionHandler(ServerWebInputException::class)
    fun handleServerWebInputException(
        exception: ServerWebInputException,
        request: ServerHttpRequest
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error("5 $exception.message", exception)

        return ResponseEntity
            .unprocessableEntity()
            .body(DefaultErrorOutput(message = exception.message, errorCode = DEFAULT_ERROR_CODE))
    }

    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun handleException(
        exception: Exception,
        request: ServerHttpRequest
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
    @ExceptionHandler(BusinessException::class)
    fun businessException(
        exception: BusinessException
    ): ResponseEntity<DefaultErrorOutput> {

        logger.error("6 $exception.message", exception)

        return ResponseEntity
            .unprocessableEntity()
            .body(
                DefaultErrorOutput(
                    message = exception.message ?: DEFAULT_MESSAGE_EXCEPTION,
                    errorCode = DONT_HAVE_AVERAGE_COST_ERROR_CODE
                )
            )
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(this::class.java)
        private val DEFAULT_MESSAGE_EXCEPTION = HttpStatus.INTERNAL_SERVER_ERROR.name
        private const val DEFAULT_ERROR_CODE = 10098
        private const val DONT_HAVE_AVERAGE_COST_ERROR_CODE = 10373
    }
}