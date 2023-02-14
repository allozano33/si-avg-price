package com.mercadolibre.si_avg_price.config.datadog

import com.mercadolibre.metrics.MetricCollector
import com.mercadolibre.metrics.Metrics
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatadogConfig {

  @Bean
  fun metricsCollector(): MetricCollector {
    return Metrics.INSTANCE
  }
}