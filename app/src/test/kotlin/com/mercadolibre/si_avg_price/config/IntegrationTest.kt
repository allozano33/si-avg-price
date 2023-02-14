package com.mercadolibre.si_avg_price.config

import com.mercadolibre.routing.RoutingHelper.createAndSetNewMeliContext
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@ActiveProfiles("test")
@Import(value = [ObjectMapperConfig::class])
abstract class IntegrationTest {

    @Autowired
    protected lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setup() {
        createAndSetNewMeliContext()
    }

}
