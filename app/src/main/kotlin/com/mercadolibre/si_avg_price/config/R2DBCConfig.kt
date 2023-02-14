package com.mercadolibre.si_avg_price.config

import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@Configuration
@Profile("!test")
@EnableR2dbcRepositories
class R2DBCConfig (private val properties: R2dbcProperties) : AbstractR2dbcConfiguration() {

    @Bean
    override fun connectionFactory(): ConnectionFactory =
        ConnectionFactoryBuilder
            .withUrl(properties.url)
            .username(properties.username)
            .password(properties.password)
            .build()

    @Bean
    fun entityTemplate(conn: ConnectionFactory): R2dbcEntityTemplate =
        R2dbcEntityTemplate(conn)
}
