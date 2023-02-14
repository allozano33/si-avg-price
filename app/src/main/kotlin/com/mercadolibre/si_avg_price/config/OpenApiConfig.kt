package com.mercadolibre.si_avg_price.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun apiEntryPointRange(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("SI avg price")
            .pathsToMatch("/**")
            .build()
    }

    @Bean
    fun apiInfo(): OpenAPI {
        val contact = Contact()
            .name("Seller Invoices - Fiscal Operations")
            .url("https://github.com/mercadolibre/fury_si-avg-price")
            .email("si-fo-tax-1p@mercadolivre.com")

        val info = Info()
            .title("si-avg-price")
            .description("API responsável por fornecer dados de preço médio de produtos em estoque.")
            .version("1.0.0")
            .contact(contact)

        return OpenAPI()
            .info(info)
    }

}