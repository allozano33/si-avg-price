package com.mercadolibre.si_avg_price.gateway

import com.mercadolibre.si_avg_price.config.configuration.SellerIdentificationProcessConfig
import com.mercadolibre.si_avg_price.gateway.config_service.SellerIdentificationProcessGatewayImpl
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class SellerIdentificationProcessGatewayImplTest {

    private lateinit var sellerIdentificationProcessConfig : SellerIdentificationProcessConfig

    private lateinit var sellerIdentificationProcessGateway : SellerIdentificationProcessGatewayImpl

    @BeforeEach
    fun setup() {
        sellerIdentificationProcessConfig = SellerIdentificationProcessConfig()
        sellerIdentificationProcessConfig.sellerIdentificationToProcess = listOf(SELLER_X, SELLER_Y)
        sellerIdentificationProcessGateway = SellerIdentificationProcessGatewayImpl(sellerIdentificationProcessConfig)
    }

    @Test
    fun `should return list of sellers`() {
        val sellersReturned = sellerIdentificationProcessGateway.getSellerIdentificationToProcess()

        Assertions.assertEquals(2, sellersReturned.size)
        Assertions.assertEquals(SELLER_X, sellersReturned[0])
        Assertions.assertEquals(SELLER_Y, sellersReturned[1])
    }

    companion object {
        private const val SELLER_X = 123456L
        private const val SELLER_Y = 123457L
    }
}