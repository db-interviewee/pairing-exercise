package io.billie.orders.service

import io.billie.common.exception.ConflictException
import io.billie.orders.data.OrderEntity
import io.billie.orders.data.OrderRepository
import io.billie.orders.viewmodel.Amount
import io.billie.orders.viewmodel.OrderRequest
import io.billie.orders.viewmodel.OrderResponse
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class OrderService(
    private val orderRepository: OrderRepository
) {

    fun create(request: OrderRequest): OrderResponse {
        return try {
            val entity = OrderEntity(
                merchantId = request.merchantId,
                buyerId = request.buyerId,
                merchantOrderId = request.merchantOrderId,
                totalAmount = request.totalAmount.amount.toBigDecimal().movePointLeft(request.totalAmount.decimal),
                orderDate = request.orderDate,
                createdAt = Instant.now()
            )

            val savedEntity = orderRepository.save(entity)
            entityToResponse(savedEntity)
        } catch (e: DbActionExecutionException) {
            if (e.cause !is DuplicateKeyException) {
                throw e
            }

            val existingOrder = orderRepository.findByMerchantIdAndMerchantOrderId(
                request.merchantId,
                request.merchantOrderId
            )!!

            val requestAmount = request.totalAmount.amount.toBigDecimal().movePointLeft(request.totalAmount.decimal)
            if (existingOrder.buyerId == request.buyerId &&
                existingOrder.totalAmount.compareTo(requestAmount) == 0 &&
                existingOrder.orderDate == request.orderDate) {

                entityToResponse(existingOrder)
            } else {
                throw ConflictException("Order already exists with different data")
            }
        }
    }

    fun findById(id: UUID): OrderResponse? {
        return orderRepository.findById(id).map { entityToResponse(it) }.orElse(null)
    }

    private fun entityToResponse(entity: OrderEntity): OrderResponse {
        return OrderResponse(
            id = entity.id!!,
            merchantId = entity.merchantId,
            merchantOrderId = entity.merchantOrderId,
            buyerId = entity.buyerId,
            orderDate = entity.orderDate,
            totalAmount = Amount(
                currency = "EUR",
                amount = entity.totalAmount.movePointRight(2).toInt(),
                decimal = 2
            ),
            createdAt = entity.createdAt
        )
    }
}
