package com.mercadolibre.si_avg_price.entrypoint.controller

import com.mercadolibre.si_avg_price.facade.ProcessAveragePriceFacade
import com.mercadolibre.si_avg_price.model.AverageCostDTO
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/average-price")
class AveragePriceController(
    private val processAveragePriceFacade: ProcessAveragePriceFacade
) {

    @GetMapping("/{cnpj}/sku/{sku}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun get(
        @PathVariable("cnpj") cnpj: String,
        @PathVariable("sku") sku: String
    ): ResponseEntity<AverageCostDTO?> =
        ResponseEntity.status(HttpStatus.OK)
            .body(processAveragePriceFacade.get(cnpj, sku))

}
