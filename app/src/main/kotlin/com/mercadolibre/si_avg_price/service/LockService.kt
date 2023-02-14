package com.mercadolibre.si_avg_price.service

import com.mercadolibre.lockclient.Lock
import com.mercadolibre.lockclient.NamespaceLockClient
import com.mercadolibre.lockclient.exceptions.LockClientException
import com.mercadolibre.si_avg_price.config.LockConfig
import com.mercadolibre.si_avg_price.exception.AlreadyLockedException
import com.mercadolibre.si_avg_price.exception.IntegrationClientErrorException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.RejectedExecutionException
import com.mercadolibre.lockclient.exceptions.AlreadyLockedException as FuryAlreadyLockedException

@Component
class LockService(
    private val lockConfig: LockConfig,
    private val lockClient: NamespaceLockClient
) {

    suspend fun lock(resource: String): Lock {
        log.debug(TRYING_TO_LOCK_MESSAGE.format(resource))

        return runCatching {
            lockClient.lock(resource, lockConfig.ttlSeconds)
        }.onSuccess {
            log.debug(LOCKED_MESSAGE.format(resource))
        }.onFailure {
            log.error(ERROR_LOCKING_MESSAGE.format(resource))

            when (it) {
                is FuryAlreadyLockedException -> throw AlreadyLockedException(
                    resource,
                    it,
                    "Could not lock $resource because already locked"
                )

                is RejectedExecutionException -> throw IntegrationClientErrorException(
                    "Could not lock $resource because was reject",
                    10098
                )

                is LockClientException -> throw IntegrationClientErrorException(
                    "Could not lock $resource because occurred a client exception",
                    it.code.toInt()
                )

                else -> throw it
            }
        }.getOrThrow()
    }

    suspend fun unlock(lock: Lock) {
        runCatching {
            lockClient.unlock(lock)
        }.onSuccess {
            log.debug(UNLOCKING_MESSAGE.format(lock.resource))
        }.onFailure {
            log.error(ERROR_UNLOCKING_MESSAGE.format(lock.resource))
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
        private const val ERROR_LOCKING_MESSAGE = "error locking resource: [document_id:%s]"
        private const val ERROR_UNLOCKING_MESSAGE = "Error unlocking [document_id:%s]"
        private const val LOCKED_MESSAGE = "locked resource: [document_id:%s]"
        private const val TRYING_TO_LOCK_MESSAGE = "trying lock resource: [document_id:%s]"
        private const val UNLOCKING_MESSAGE = "unlocking resource: [document_id:%s]"
    }
}