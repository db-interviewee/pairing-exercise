package io.billie.orders.resource

import io.billie.orders.viewmodel.Amount
import io.billie.orders.viewmodel.OrderRequest
import io.billie.orders.viewmodel.OrderResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@RestController
@RequestMapping("orders")
class OrderResource {
    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Notify Billie of a new order",
        description = "Merchants use this endpoint to notify Billie when an order has been placed by a buyer. " +
                "This allows Billie to prepare for payment processing upon shipment notification. " +
                "This endpoint is idempotent - multiple requests with the same merchant_order_id and identical data " +
                "will always return 201 Created with the same order details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Order notified successfully (idempotent)",
                content = [Content(schema = Schema(implementation = OrderResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request",
                content = [Content()]
            ),
            ApiResponse(
                responseCode = "409",
                description = "A different order has already been notified with the same ID",
                content = [Content()]
            )
        ]
    )
    fun postOrder(@RequestBody orderRequest: OrderRequest): ResponseEntity<OrderResponse> {
        val fakeResponse = OrderResponse(
            id = UUID.randomUUID(),
            organizationId = orderRequest.organizationId,
            merchantOrderId = orderRequest.merchantOrderId,
            buyerId = orderRequest.buyerId,
            orderDate = orderRequest.orderDate,
            totalAmount = orderRequest.totalAmount,
            createdAt = Instant.now()
        )
        val location = URI("/orders/${fakeResponse.id}")
        return ResponseEntity.created(location).body(fakeResponse)
    }

    @GetMapping("/{id}")
    fun getOrder(@PathVariable id: UUID): OrderResponse {
        return OrderResponse(
            id = id,
            organizationId = UUID.fromString("6dc7acb9-3fba-4c5b-bd0c-6898b6ec152a"),
            merchantOrderId = "689a4d192c1d62d71289b03d",
            buyerId = UUID.fromString("46aac123-9cee-4a5f-9986-386eb36bd70e"),
            orderDate = Instant.parse("2025-08-11T14:30:00Z"),
            totalAmount = Amount(currency = "EUR", amount = 10000, decimal = 2),
            createdAt = Instant.parse("2025-08-11T14:31:15Z")
        )
    }
}