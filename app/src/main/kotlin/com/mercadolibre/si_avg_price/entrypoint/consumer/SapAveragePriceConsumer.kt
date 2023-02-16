package com.mercadolibre.si_avg_price.entrypoint.consumer

import com.mercadolibre.restclient.log.LogUtil.log
import com.mercadolibre.si_avg_price.entrypoint.filter.EntryPointFilter
import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.input.SapInput
import com.mercadolibre.si_avg_price.entrypoint.resource.consumer.output.SapOutput
import com.mercadolibre.si_avg_price.extension_function.executeWithLock
import com.mercadolibre.si_avg_price.facade.ProcessAveragePriceFacade
import com.mercadolibre.si_avg_price.service.LockService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/consumer/sap/average-price/process")
class SapAveragePriceConsumer(
    private val entryPointFilter: EntryPointFilter,
    private val lockService: LockService,
    private val processAveragePriceFacade: ProcessAveragePriceFacade
) {

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun process(
        @RequestBody msg: String
    ): ResponseEntity<SapOutput> {
        log.info("sap message $msg")
        return entryPointFilter.readMessage(msg, SapInput::class.java).toDomain().let { sapInput ->
            log.info("sap input $sapInput")
            lockService.executeWithLock("${sapInput.sku}-${sapInput.cnpj}") {
                processAveragePriceFacade.execute(sapInput)
            }.let {
                ResponseEntity.ok().body(it)
            }
        }
    }
}
