package io.billie.orders.data

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Table("orders")
data class OrderEntity(
    @Id
    val id: UUID? = null,

    @Column("merchant_id")
    val merchantId: UUID,

    @Column("buyer_id")
    val buyerId: UUID,

    @Column("merchant_order_id")
    val merchantOrderId: String,

    @Column("total_amount")
    val totalAmount: BigDecimal,

    @Column("order_date")
    val orderDate: Instant,

    @Column("created_at")
    val createdAt: Instant
)
