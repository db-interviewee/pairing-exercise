package io.billie.orders.resource

import io.billie.orders.service.OrderService
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
import java.util.UUID

@RestController
@RequestMapping("orders")
class OrderResource(
    private val orderService: OrderService
) {
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
        val createdOrder = orderService.create(orderRequest)
        val location = URI("/orders/${createdOrder.id}")
        return ResponseEntity.created(location).body(createdOrder)
    }

    @GetMapping(
        "/{id}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Get order by ID",
        description = "Retrieves an order by its Billie internal ID"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Order found",
                content = [Content(schema = Schema(implementation = OrderResponse::class))]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Order not found",
                content = [Content()]
            )
        ]
    )
    fun getOrder(@PathVariable id: UUID): ResponseEntity<OrderResponse> {
        val order = orderService.findById(id)
        return if (order != null) {
            ResponseEntity.ok(order)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}