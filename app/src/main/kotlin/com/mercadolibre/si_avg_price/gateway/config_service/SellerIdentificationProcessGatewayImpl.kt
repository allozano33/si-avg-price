package com.mercadolibre.si_avg_price.gateway.config_service

import com.mercadolibre.si_avg_price.config.configuration.SellerIdentificationProcessConfig
import org.springframework.stereotype.Component

@Component
class SellerIdentificationProcessGatewayImpl(
    private val sellerIdentificationToProcess: SellerIdentificationProcessConfig
) : SellerIdentificationProcessGateway {
    override fun getSellerIdentificationToProcess(): List<Long> =
        sellerIdentificationToProcess.sellerIdentificationToProcess
}