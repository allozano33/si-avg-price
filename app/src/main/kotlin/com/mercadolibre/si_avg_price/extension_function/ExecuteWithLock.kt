package com.mercadolibre.si_avg_price.extension_function

import com.mercadolibre.si_avg_price.service.LockService

suspend fun <T> LockService.executeWithLock(resource: String, block: suspend () -> T): T {
    val lock = this.lock(resource)
    return runCatching {
        block()
    }.onSuccess {
        this.unlock(lock)
    }.onFailure {
        this.unlock(lock)
    }.getOrThrow()
}