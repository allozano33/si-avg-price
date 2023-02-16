package com.mercadolibre.si_avg_price.resourse.database

import com.mercadolibre.si_avg_price.model.AverageCostDTO
import com.mercadolibre.si_avg_price.model.AveragePriceProcess
import org.springframework.data.annotation.Id
import org.springframework.data.domain.Persistable
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("average_price")
data class AveragePriceDB(
    @Id
    val id: Long = 0,
    val sku: String,
    val cnpj: String,
    val cost: BigDecimal,
    val stock: BigDecimal,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) : Persistable<Long> {

    companion object {
        fun of(averagePriceProcess: AveragePriceProcess) =
            AveragePriceDB(
                sku = averagePriceProcess.sku,
                cnpj = averagePriceProcess.cnpj,
                cost = averagePriceProcess.averagePrice,
                stock = averagePriceProcess.stock,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )

        fun saveAndUpdate(
            averageCostDTO: AverageCostDTO,
            averagePriceProcess: AveragePriceProcess
        ) =
            AveragePriceDB(
                id = averageCostDTO.id!!,
                sku = averagePriceProcess.sku,
                cnpj = averagePriceProcess.cnpj,
                cost = averagePriceProcess.averagePrice,
                stock = averagePriceProcess.stock,
                createdAt = averageCostDTO.createdAt,
                updatedAt = LocalDateTime.now()
            )
    }

    fun toDomain() =
        AverageCostDTO(
            id = id,
            sku = sku,
            cnpj = cnpj,
            averagePrice = cost,
            stock = stock,
            createdAt = createdAt,
            updatedAt = updatedAt
        )

    override fun getId(): Long = id

    override fun isNew(): Boolean = id <= 0
}
