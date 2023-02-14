package com.mercadolibre.si_avg_price.gateway.mock

import com.mercadolibre.common.async.Callback
import com.mercadolibre.lockclient.Lock
import com.mercadolibre.lockclient.LockClient
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.Callable
import java.util.concurrent.Future

@Component
class LocalLock: LockClient {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun lock(namespace: String, resource: String): Lock {
        log.info("locking resource: $resource")
        val lock = Lock()
        lock.resource = resource
        lock.token = StringUtils.EMPTY
        return lock
    }

    override fun lock(namespace: String, resource: String, ttl: Long): Lock {
        return lock(namespace, resource)
    }

    override fun <T : Any?> lock(p0: String?, p1: String?, p2: Callable<T>?, p3: Callback<T>?): Future<T> {
        TODO("Not yet implemented")
    }

    override fun <T : Any?> lock(p0: String?, p1: String?, p2: Callable<T>?): Future<T> {
        TODO("Not yet implemented")
    }

    override fun get(p0: String?, p1: String?): Lock {
        TODO("Not yet implemented")
    }

    override fun keepAlive(p0: String?, p1: Lock?): Lock {
        TODO("Not yet implemented")
    }

    override fun unlock(namespace: String, lock: Lock) {
        log.info("unlocking resource: ${lock.resource}")
    }
}