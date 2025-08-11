package io.billie.shipments.data

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Table("shipments")
data class ShipmentEntity(
    @Id
    val id: UUID? = null,

    @Column("order_id")
    val orderId: UUID,

    @Column("merchant_shipment_id")
    val merchantShipmentId: String,

    @Column("courier")
    val courier: String,

    @Column("tracking_id")
    val trackingId: String,

    @Column("shipped_amount")
    val shippedAmount: BigDecimal,

    @Column("shipped_at")
    val shippedAt: Instant,

    @Column("created_at")
    val createdAt: Instant
)
