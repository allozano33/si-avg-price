package com.mercadolibre.si_avg_price.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolibre.fury.core.routing.spring.reactive.MeliTracingExchangeFilter
import io.netty.channel.ChannelOption.CONNECT_TIMEOUT_MILLIS
import io.netty.handler.logging.LogLevel
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.time.Duration.ofMillis

@Configuration
class WebClientConfig(val apiConfig: ApiConfig) {

    @Bean
    @Primary
    fun meliWebClient(exchangeStrategies: ExchangeStrategies, httpClient: HttpClient) =
        buildWebClient(apiConfig.baseUri, exchangeStrategies, httpClient)

    fun buildWebClient(baseUrl: String, exchangeStrategies: ExchangeStrategies, httpClient: HttpClient) =
        WebClient.builder()
            .baseUrl(baseUrl)
            .filter(MeliTracingExchangeFilter())
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .exchangeStrategies(exchangeStrategies)
            .build()

    @Bean
    @Profile("production")
    fun httpClient(): HttpClient =
        HttpClient.create()
            .option(CONNECT_TIMEOUT_MILLIS, 1000)
            .responseTimeout(ofMillis(1000))
            .compress(true)

    @Bean
    @Profile("!production")
    fun httpClientStage(): HttpClient =
        HttpClient.create()
            .option(CONNECT_TIMEOUT_MILLIS, 2000)
            .compress(true)
            .wiretap(
                "reactor.netty.http.client.HttpClient",
                LogLevel.INFO,
                AdvancedByteBufFormat.TEXTUAL,
                Charsets.UTF_8
            )
            .doOnConnected {
                it.addHandlerLast(ReadTimeoutHandler(30))
                    .addHandlerLast(WriteTimeoutHandler(30))
            }

    @Bean
    fun exchangeStrategies(objectMapper: ObjectMapper): ExchangeStrategies =
        ExchangeStrategies
            .builder()
            .codecs { clientDefaultCodecsConfigure ->
                clientDefaultCodecsConfigure.defaultCodecs()
                    .jackson2JsonEncoder(Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON))
                clientDefaultCodecsConfigure.defaultCodecs()
                    .jackson2JsonDecoder(Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON))
                clientDefaultCodecsConfigure.defaultCodecs().maxInMemorySize(MB_4)
            }.build()

    companion object {
        private const val MB_4 = 4000000
    }
}