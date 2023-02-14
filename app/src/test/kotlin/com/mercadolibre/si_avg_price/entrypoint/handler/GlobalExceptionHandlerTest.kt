package com.mercadolibre.si_avg_price.entrypoint.handler

import com.mercadolibre.si_avg_price.entrypoint.resource.handler.DefaultErrorOutput
import com.mercadolibre.si_avg_price.exception.AlreadyLockedException
import com.mercadolibre.si_avg_price.exception.IntegrationClientErrorException
import com.mercadolibre.si_avg_price.exception.IntegrationServerErrorException
import com.mercadolibre.si_avg_price.exception.NotFoundLockedException
import io.mockk.clearAllMocks
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpRequest

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
        val request: ServerHttpRequest = mock(ServerHttpRequest::class.java)


        val error =
            globalExceptionHandler.handleIntegrationClientErrorException(e, request)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), error.statusCodeValue)
        Assertions.assertEquals("message", errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs IntegrationServerErrorException - should return error`() {

        val e = IntegrationServerErrorException(message = ERROR_MESSAGE, code = DEFAULT_ERROR_CODE)
        val request: ServerHttpRequest = mock(ServerHttpRequest::class.java)


        val error =
            globalExceptionHandler.handleIntegrationServerErrorException(e, request)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.statusCodeValue)
        Assertions.assertEquals("message", errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs AlreadyLockedException - should return error`() {

        val e =
            AlreadyLockedException(message = ERROR_MESSAGE, resource = UUID, cause = Exception())
        val request: ServerHttpRequest = mock(ServerHttpRequest::class.java)


        val error =
            globalExceptionHandler.handleAlreadyLockedException(e, request)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.OK.value(), error.statusCodeValue)
        Assertions.assertEquals("message", errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs NotFoundLockedException - should return error`() {

        val e = NotFoundLockedException(message = ERROR_MESSAGE, cause = Exception())
        val request: ServerHttpRequest = mock(ServerHttpRequest::class.java)


        val error =
            globalExceptionHandler.handleNotFoundLockedException(e, request)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.OK.value(), error.statusCodeValue)
        Assertions.assertEquals("message", errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs Exception or Exception not mapped - should return error`() {

        val e = Exception(ERROR_MESSAGE)
        val request: ServerHttpRequest = mock(ServerHttpRequest::class.java)


        val error =
            globalExceptionHandler.handleException(e, request)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.statusCodeValue)
        Assertions.assertEquals(ERROR_MESSAGE, errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    @Test
    fun `when occurs Exception or Exception not mapped - should return error with message DEFAULT`() {

        val e = Exception()
        val request: ServerHttpRequest = mock(ServerHttpRequest::class.java)

        val error =
            globalExceptionHandler.handleException(e, request)

        val errorObject: DefaultErrorOutput = error.body as DefaultErrorOutput

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), error.statusCodeValue)
        Assertions.assertEquals(DEFAULT_MESSAGE_EXCEPTION, errorObject.message)
        Assertions.assertEquals(DEFAULT_ERROR_CODE, errorObject.errorCode)
    }

    companion object {
        private const val ERROR_MESSAGE = "message"
        private val DEFAULT_MESSAGE_EXCEPTION = HttpStatus.INTERNAL_SERVER_ERROR.name
        private const val DEFAULT_ERROR_CODE = 10098
        private const val UUID = "uuid"
    }

}