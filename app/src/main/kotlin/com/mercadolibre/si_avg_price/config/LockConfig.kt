package com.mercadolibre.si_avg_price.config

import com.mercadolibre.lockclient.LockApiClient
import com.mercadolibre.lockclient.NamespaceLockClient
import com.mercadolibre.lockclient.config.EndpointType
import com.mercadolibre.lockclient.config.LockConfiguration
import com.mercadolibre.lockclient.config.retry.strategy.Simple
import com.mercadolibre.si_avg_price.gateway.mock.LocalLock
import org.apache.commons.lang3.StringUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@ConfigurationProperties(prefix = "fury-lock")
class LockConfig {
    var socketTimeout: Int = 0
    var maxWaitValue: Int = 0
    var connectionTimeout: Int = 0
    var maxConnections: Int = 0
    var maxConnectionsPerRoute: Int = 0
    var maxRetries: Int = 0
    var retryDelay: Long = 0L
    var namespace: String = StringUtils.EMPTY
    var ttlSeconds: Long = 0L
    var defaultEndpoint = StringUtils.EMPTY

    @Bean
    fun getConfig(): LockConfiguration {
        val lockConfigBuilder = LockConfiguration
            .builder()
            .withSocketTimeout(socketTimeout)
            .withMaxWait(maxWaitValue)
            .withConnectionTimeout(connectionTimeout)
            .withMaxConnections(maxConnections)
            .withMaxConnectionsPerRoute(maxConnectionsPerRoute)
            .withRetryStrategy(Simple(maxRetries, retryDelay))

        if (defaultEndpoint.isNotEmpty()) {
            lockConfigBuilder.withDefaultEndpoint(EndpointType.READ, defaultEndpoint)
            lockConfigBuilder.withDefaultEndpoint(EndpointType.WRITE, defaultEndpoint)
        }

        return lockConfigBuilder.build()
    }

    @Bean
    @Profile("stage", "production")
    fun lockClient(config: LockConfiguration) =
        NamespaceLockClient(LockApiClient(config), namespace)


    @Bean("lockClient")
    @Profile("test", "development", "mock")
    fun lockClientLocal(config: LockConfiguration) =
        NamespaceLockClient(LocalLock(), namespace)
}
