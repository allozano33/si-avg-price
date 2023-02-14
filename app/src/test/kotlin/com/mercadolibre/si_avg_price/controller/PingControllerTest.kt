package com.mercadolibre.si_avg_price.controller

import com.mercadolibre.si_avg_price.entrypoint.controller.PingController
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.test.web.reactive.server.WebTestClient

class PingControllerTest {

    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setup() {
        webTestClient = WebTestClient.bindToController(PingController()).configureClient().build()
    }
    @Test
    fun `ping should return pong`() {
        webTestClient.get()
            .uri("/ping")
            .exchange()
            .expectStatus().isOk
    }
}
