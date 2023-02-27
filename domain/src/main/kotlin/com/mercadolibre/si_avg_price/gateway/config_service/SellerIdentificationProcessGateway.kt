package com.mercadolibre.si_avg_price.gateway.config_service

interface SellerIdentificationProcessGateway {
    fun getSellerIdentificationToProcess(): List<Long>
}