package io.billie.orders.data

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface OrderRepository : CrudRepository<OrderEntity, UUID> {
    fun findByMerchantIdAndMerchantOrderId(merchantId: UUID, merchantOrderId: String): OrderEntity?
}
