package com.mercadolibre.si_avg_price.config

import io.r2dbc.spi.ConnectionFactory
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator
import org.springframework.test.context.ActiveProfiles

@DataR2dbcTest
@ActiveProfiles("database")
@TestInstance(PER_CLASS)
open class DatabaseTest {

    @Autowired
    protected lateinit var conn: ConnectionFactory

    @BeforeAll
    fun beforeAll() {
        val resourceLoader: ResourceLoader = DefaultResourceLoader()
        val scripts = arrayOf(resourceLoader.getResource("classpath:db/init.sql"))
        ResourceDatabasePopulator(*scripts).populate(conn).block()
    }

}