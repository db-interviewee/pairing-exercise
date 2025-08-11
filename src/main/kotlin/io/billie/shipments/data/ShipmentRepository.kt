package io.billie.shipments.data

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ShipmentRepository : CrudRepository<ShipmentEntity, UUID> {
    fun findByOrderId(orderId: UUID): List<ShipmentEntity>
    fun findByOrderIdAndMerchantShipmentId(orderId: UUID, merchantShipmentId: String): ShipmentEntity?
}
