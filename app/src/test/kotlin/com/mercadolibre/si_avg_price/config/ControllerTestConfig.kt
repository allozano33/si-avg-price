package com.mercadolibre.si_provision_assembler.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolibre.si_avg_price.entrypoint.filter.EntryPointFilter
import com.mercadolibre.si_avg_price.gateway.DatadogGateway
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("test")
class ControllerTestConfig {

    @Bean
    fun entrypointFilter(
        datadogGateway: DatadogGateway,
        objectMapper: ObjectMapper
    ): EntryPointFilter =
        EntryPointFilter(objectMapper, datadogGateway)
}