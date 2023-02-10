package com.mercadolibre.si_avg_price.entrypoint.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolibre.si_avg_price.config.ObjectMapperConfig
import com.mercadolibre.si_avg_price.gateway.DatadogGateway
import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearAllMocks
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Import
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@Import(value = [ObjectMapperConfig::class, Jackson2ObjectMapperBuilder::class])
internal class EntryPointFilterTest {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var datadogGateway: DatadogGateway

    private lateinit var entryPointFilter: EntryPointFilter

    @BeforeEach
    fun setUp() {
        clearAllMocks()
        entryPointFilter = EntryPointFilter(objectMapper, datadogGateway)
    }
}

