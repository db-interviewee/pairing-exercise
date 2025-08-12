package io.billie.shipments.service

import io.billie.common.exception.ConflictException
import io.billie.orders.exception.OrderNotFoundException
import io.billie.orders.service.OrderService
import io.billie.orders.viewmodel.Amount
import io.billie.shipments.data.ShipmentEntity
import io.billie.shipments.data.ShipmentRepository
import io.billie.shipments.exception.CurrencyMismatchException
import io.billie.shipments.exception.ShipmentExceedsOrderException
import io.billie.shipments.viewmodel.ShipmentRequest
import io.billie.shipments.viewmodel.ShipmentResponse
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Service
class ShipmentService(
    private val shipmentRepository: ShipmentRepository,
    private val orderService: OrderService
) {

    fun create(request: ShipmentRequest): ShipmentResponse {
        val order = orderService.findById(request.orderId)
            ?: throw OrderNotFoundException(request.orderId)

        if (request.shippedAmount.currency != order.totalAmount.currency) {
            throw CurrencyMismatchException(order.totalAmount.currency, request.shippedAmount.currency)
        }

        val existingShipments = shipmentRepository.findByOrderId(request.orderId)

        val totalShipped = existingShipments
            .map { it.shippedAmount }
            .fold(BigDecimal.ZERO) { acc, amount -> acc.add(amount) }

        val requestAmount = request.shippedAmount.amount.toBigDecimal().movePointLeft(request.shippedAmount.decimal)
        val newTotal = totalShipped.add(requestAmount)

        val orderTotal = order.totalAmount.amount.toBigDecimal().movePointLeft(order.totalAmount.decimal)
        if (newTotal.compareTo(orderTotal) > 0) {
            throw ShipmentExceedsOrderException(
                "Shipment would exceed order total. Order total: $orderTotal, Already shipped: $totalShipped, Attempting to ship: $requestAmount"
            )
        }
        val entity = ShipmentEntity(
            orderId = request.orderId,
            merchantShipmentId = request.merchantShipmentId,
            courier = request.courier,
            trackingId = request.trackingId,
            shippedAmount = requestAmount,
            shippedAt = request.shippedAt,
            createdAt = Instant.now()
        )

        return try {
            val savedEntity = shipmentRepository.save(entity)
            entityToResponse(savedEntity)
        } catch (e: DbActionExecutionException) {
            if (e.cause !is DuplicateKeyException) {
                throw e
            }

            val existingShipment = shipmentRepository.findByOrderIdAndMerchantShipmentId(
                request.orderId,
                request.merchantShipmentId
            )!!

            if (existingShipment.shippedAmount.compareTo(requestAmount) == 0 &&
                existingShipment.courier == request.courier &&
                existingShipment.trackingId == request.trackingId &&
                existingShipment.shippedAt == request.shippedAt
            ) {
                entityToResponse(existingShipment)
            } else {
                throw ConflictException("Shipment already exists with different data")
            }
        }
    }

    fun findByOrderId(orderId: UUID): List<ShipmentResponse> {
        return shipmentRepository.findByOrderId(orderId)
            .map { entityToResponse(it) }
    }

    private fun entityToResponse(entity: ShipmentEntity): ShipmentResponse {
        return ShipmentResponse(
            id = entity.id!!,
            orderId = entity.orderId,
            merchantShipmentId = entity.merchantShipmentId,
            courier = entity.courier,
            trackingId = entity.trackingId,
            shippedAmount = Amount(
                currency = "EUR",
                amount = entity.shippedAmount.movePointRight(2).toInt(),
                decimal = 2
            ),
            shippedAt = entity.shippedAt,
            createdAt = entity.createdAt
        )
    }
}
