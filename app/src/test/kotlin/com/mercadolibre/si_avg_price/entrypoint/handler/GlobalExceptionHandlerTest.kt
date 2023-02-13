package com.mercadolibre.si_avg_price.entrypoint.handler

import com.mercadolibre.si_avg_price.entrypoint.resource.handler.DefaultErrorOutput
import com.mercadolibre.si_avg_price.exception.*
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus

@ExtendWith(MockKExtension::class)
class GlobalExceptionHandlerTest {

    private lateinit var globalExceptionHandler: GlobalExceptionHandler

    @BeforeEach
    fun setup() {
        clearAllMocks()
        globalExceptionHandler = GlobalExceptionHandler()
    }

    @Test
    fun `when occurs IntegrationClientErrorException - should return error`() {

        val e = IntegrationClientErrorException(message = ERROR_MESSAGE, code = DEFAULT_ERROR_CODE)

        val error =
            globalExceptionHandler.handleIntegrationClientErrorException(e)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), error.statusCodeValue)
        Assertions.assertEquals("message", errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs IntegrationServerErrorException - should return error`() {

        val e = IntegrationServerErrorException(message = ERROR_MESSAGE, code = DEFAULT_ERROR_CODE)

        val error =
            globalExceptionHandler.handleIntegrationServerErrorException(e)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.statusCodeValue)
        Assertions.assertEquals("message", errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs BusinessException - when came should retry true - should return error`() {

        val e = BusinessException(
            message = DEFAULT_MESSAGE_EXCEPTION,
            code = DEFAULT_ERROR_CODE,
            shouldRetry = true
        )

        val error =
            globalExceptionHandler.handleBusinessException(e)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), error.statusCodeValue)
        Assertions.assertEquals(DEFAULT_MESSAGE_EXCEPTION, errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs BusinessException - when came should retry false - should return error`() {

        val e = BusinessException(
            message = DEFAULT_MESSAGE_EXCEPTION,
            code = DEFAULT_ERROR_CODE,
            shouldRetry = false
        )

        val error =
            globalExceptionHandler.handleBusinessException(e)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.OK.value(), error.statusCodeValue)
        Assertions.assertEquals(DEFAULT_MESSAGE_EXCEPTION, errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs Exception or Exception not mapped - should return error`() {

        val e = Exception(ERROR_MESSAGE)

        val error =
            globalExceptionHandler.handleException(e)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.statusCodeValue)
        Assertions.assertEquals(ERROR_MESSAGE, errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs Exception or Exception not mapped - should return error with message DEFAULT`() {

        val e = Exception()

        val error =
            globalExceptionHandler.handleException(e)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.statusCodeValue)
        Assertions.assertEquals(DEFAULT_MESSAGE_EXCEPTION, errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    companion object {
        private const val ERROR_MESSAGE = "message"
        private val DEFAULT_MESSAGE_EXCEPTION = HttpStatus.INTERNAL_SERVER_ERROR.name
        private const val DEFAULT_ERROR_CODE = 10098
    }

}