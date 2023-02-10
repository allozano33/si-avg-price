package com.mercadolibre.si_avg_price.core

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.SpringApplication
import org.springframework.mock.env.MockEnvironment

class ScopeProfileActivationEnvironmentPostProcessorTest {

    private val processor = ScopeProfileActivationEnvironmentPostProcessor()

    @Test
    fun `postProcessEnvironment should not add active profile when there is no SCOPE`() {
        val environment = MockEnvironment()
        val application = SpringApplication()
        processor.postProcessEnvironment(environment, application)
        assertTrue(environment.activeProfiles.isEmpty())
    }

    @Test
    fun `postProcessEnvironment should add active profile with SCOPE value when there is no - separator`() {
        val environment = MockEnvironment().withProperty("SCOPE", "api")
        val application = SpringApplication()
        processor.postProcessEnvironment(environment, application)
        assertEquals("api", environment.activeProfiles[0])
    }

    @Test
    fun `postProcessEnvironment should add active profile with SCOPE suffix separated by -`() {
        val environment = MockEnvironment().withProperty("SCOPE", "my-api-test")
        val application = SpringApplication()
        processor.postProcessEnvironment(environment, application)
        assertEquals("test", environment.activeProfiles[0])
    }
}
