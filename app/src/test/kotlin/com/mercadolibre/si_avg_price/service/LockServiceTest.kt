package com.mercadolibre.si_avg_price.service

import com.mercadolibre.lockclient.Lock
import com.mercadolibre.lockclient.NamespaceLockClient
import com.mercadolibre.lockclient.exceptions.LockClientException
import com.mercadolibre.si_avg_price.config.LockConfig
import com.mercadolibre.si_avg_price.exception.AlreadyLockedException
import com.mercadolibre.si_avg_price.exception.BusinessException
import com.mercadolibre.si_avg_price.exception.DefaultErrorException
import com.mercadolibre.si_avg_price.exception.IntegrationClientErrorException
import com.mercadolibre.si_avg_price.extension_function.executeWithLock
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.http.HttpStatus.BAD_REQUEST
import java.util.concurrent.RejectedExecutionException
import com.mercadolibre.lockclient.exceptions.AlreadyLockedException as FuryAlreadyLockedException

@ExtendWith(MockKExtension::class)
class LockServiceTest {

    @MockK
    private lateinit var lockConfig: LockConfig

    @MockK
    private lateinit var lockClient: NamespaceLockClient

    private lateinit var lockService: LockService

    @BeforeEach
    fun setUp() {
        lockService = LockService(lockConfig, lockClient)

        every { lockConfig.namespace } returns NAMESPACE
        every { lockConfig.ttlSeconds } returns 1L
    }

    @Test
    fun `given a resource - should lock and unlock successfully`() {
        lockAndUnlockMock()

        val result = runBlocking {
            lockService.executeWithLock(RESOURCE) {}
        }

        Assertions.assertNotNull(result)
        verify(exactly = 1) { lockClient.lock(any(), any())  }
        verify(exactly = 1) { lockClient.unlock(any())  }
    }

    @Test
    fun `should lock successfully`() {
        lockAndUnlockMock()

        val result = runBlocking { lockService.lock(RESOURCE) }

        Assertions.assertNotNull(result)
        Assertions.assertEquals(RESOURCE, result.resource)
        Assertions.assertEquals("token", result.token)

        verify(exactly = 1) { lockClient.lock(any(), any()) }
    }

    @Test
    fun `should unlock successfully and call lockClient once`() {
        justRun { lockClient.unlock(any()) }

        runBlocking { lockService.unlock(lockedResource) }

        verify(exactly = 1) { lockClient.unlock(lockedResource) }
    }

    @Test
    fun `given locked resource should throw AlreadyLockedException`() {
        every {
            lockClient.lock(any(), any())
        } throws FuryAlreadyLockedException(
            "Could not lock $RESOURCE because already locked",
            0
        )

        assertThrows<AlreadyLockedException> {
            runBlocking { lockService.lock(RESOURCE) }
        }
    }

    @Test
    fun `given many requests to lock - should return integration client exception`() {
        every {
            lockClient.lock(any(), any())
        } throws RejectedExecutionException("Could not lock $RESOURCE because was reject")

        assertThrows<IntegrationClientErrorException> {
            runBlocking { lockService.lock(RESOURCE) }
        }
    }

    @Test
    fun `when try lock - fury lock return bad request - should return http error exception`() {
        every {
            lockClient.lock(any(), any())
        } throws LockClientException(
            "Could not lock $RESOURCE because occurred a client exception",
            "1000",
            BAD_REQUEST.value(),
            Throwable()
        )

        assertThrows<IntegrationClientErrorException> {
            runBlocking { lockService.lock(RESOURCE) }
        }
    }

    @Test
    fun `given generic exception - should return it`() {
        every {
            lockClient.lock(any(), any())
        } throws DefaultErrorException("Could not lock $RESOURCE", 10098)

        assertThrows<DefaultErrorException> {
            runBlocking { lockService.lock(RESOURCE) }
        }
    }

    @Test
    fun `when execution block fails - should unlock`() {
        lockAndUnlockMock()

        assertThrows<BusinessException> {
            runBlocking {
                lockService.executeWithLock(RESOURCE) {
                    throw BusinessException("TEST ERROR", 10098)
                }
            }
        }
        verify(exactly = 1) { lockClient.unlock(lockedResource) }
    }

    private fun lockAndUnlockMock() {
        every { lockClient.lock(any(), any()) } returns lockedResource
        justRun { lockClient.unlock(any()) }
    }

    companion object {
        private val lockedResource = Lock().apply {
            resource = RESOURCE; token = "token"
        }
        private const val RESOURCE = "testResource"
        private const val NAMESPACE = "test-namespace"
    }
}