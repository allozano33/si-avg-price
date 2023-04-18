package com.mercadolibre.si_avg_price.entrypoint.controller

import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.output.SapOutput
import com.mercadolibre.si_avg_price.facade.ProcessAveragePriceFacade
import com.mercadolibre.si_avg_price.model.AverageCostDTO
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/average-price")
class AveragePriceController(
    private val processAveragePriceFacade: ProcessAveragePriceFacade
) {

    @GetMapping("/cnpj/{cnpj}/sku/{sku}", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun get(
        @PathVariable("cnpj") cnpj: String,
        @PathVariable("sku") sku: String
    ): ResponseEntity<AverageCostDTO?> =
        ResponseEntity.status(HttpStatus.OK)
            .body(processAveragePriceFacade.get(cnpj, removeZero(sku)))

    @GetMapping("/listAll", produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getAll(): ResponseEntity<List<AverageCostDTO>> =
        ResponseEntity.status(HttpStatus.OK)
            .body(processAveragePriceFacade.getAll())

    @PostMapping(
        "/insert",
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun insert(
        @Validated @RequestBody averageCostDTO: AverageCostDTO,
    ): ResponseEntity<SapOutput?> =
        ResponseEntity.status(HttpStatus.OK)
            .body(
                processAveragePriceFacade.execute(
                    AveragePriceProcess(
                        sku = averageCostDTO.sku,
                        cnpj = averageCostDTO.cnpj,
                        stock = averageCostDTO.stock,
                        averagePrice = averageCostDTO.averagePrice,
                        dateUpdate = averageCostDTO.updatedAt
                    )
                )
            )

    private fun removeZero(sku: String): String {
        var auxSku = sku
        while (auxSku.toCharArray()[0].toString().equals("0")) {
            auxSku = auxSku.drop(1)
        }
        return auxSku
    }
}
