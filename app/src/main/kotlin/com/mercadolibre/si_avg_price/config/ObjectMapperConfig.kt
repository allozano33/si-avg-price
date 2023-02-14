package com.mercadolibre.si_avg_price.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
import com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL
import com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SNAKE_CASE
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
import com.fasterxml.jackson.databind.SerializationFeature.WRITE_ENUMS_USING_TO_STRING
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper =
        builder
            .propertyNamingStrategy(SNAKE_CASE)
            .featuresToDisable(
                WRITE_DATES_AS_TIMESTAMPS,
                FAIL_ON_UNKNOWN_PROPERTIES
            )
            .featuresToEnable(
                ACCEPT_CASE_INSENSITIVE_ENUMS,
                WRITE_ENUMS_USING_TO_STRING,
                READ_UNKNOWN_ENUM_VALUES_AS_NULL
            )
            .serializationInclusion(JsonInclude.Include.NON_EMPTY)
            .simpleDateFormat(ISO_8601_24H_FULL_FORMAT)
            .modules(
                JavaTimeModule(),
                Jdk8Module(),
                KotlinModule.Builder().build(),
                enumLowerCaseModule())
            .build()

    fun enumLowerCaseModule() = SimpleModule("enum-module").apply {
        addSerializer(object : StdSerializer<Enum<*>>(Enum::class.java) {
            override fun serialize(value: Enum<*>, gen: JsonGenerator, provider: SerializerProvider) {
                gen.writeString(value.name.lowercase())
            }
        })
    }

    companion object {
        const val ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
    }
}