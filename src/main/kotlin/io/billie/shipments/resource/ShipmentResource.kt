package io.billie.shipments.resource

import io.billie.shipments.service.ShipmentService
import io.billie.shipments.viewmodel.ShipmentRequest
import io.billie.shipments.viewmodel.ShipmentResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("shipments")
class ShipmentResource(
    private val shipmentService: ShipmentService
) {

    @PostMapping(
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Notify Billie of a shipment",
        description = "Merchants use this endpoint to notify Billie when items have been shipped. " +
                "The sum of all shipments for an order cannot exceed the total order amount. " +
                "This endpoint is idempotent - multiple requests with the same merchant_shipment_id and identical data " +
                "will always return 201 Created with the same shipment details."
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                description = "Shipment notified successfully",
                content = [Content(schema = Schema(implementation = ShipmentResponse::class))]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Bad request (e.g., shipment exceeds order total)",
                content = []
            ),
            ApiResponse(
                responseCode = "404",
                description = "Order not found",
                content = []
            ),
            ApiResponse(
                responseCode = "409",
                description = "A different shipment has already been notified with the same merchant_shipment_id",
                content = []
            )
        ]
    )
    fun postShipment(@RequestBody shipmentRequest: ShipmentRequest): ResponseEntity<ShipmentResponse> {
        val createdShipment = shipmentService.create(shipmentRequest)
        val location = URI("/shipments/${createdShipment.id}")
        return ResponseEntity.created(location).body(createdShipment)
    }

    @GetMapping(
        "/order/{orderId}",
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    @Operation(
        summary = "Get all shipments for an order",
        description = "Retrieves all shipments that have been made for a specific order"
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "List of shipments for the order",
                content = [Content(array = ArraySchema(schema = Schema(implementation = ShipmentResponse::class)))]
            )
        ]
    )
    fun getShipmentsByOrder(@PathVariable orderId: UUID): ResponseEntity<List<ShipmentResponse>> {
        val shipments = shipmentService.findByOrderId(orderId)
        return ResponseEntity.ok(shipments)
    }
}
