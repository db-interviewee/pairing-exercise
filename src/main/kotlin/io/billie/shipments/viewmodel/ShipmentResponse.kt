package io.billie.shipments.viewmodel

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.orders.viewmodel.Amount
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.*

data class ShipmentResponse(
    @JsonProperty("id")
    @Schema(description = "Billie's internal shipment ID", example = "456e7890-e12b-34d5-a678-123456789012")
    val id: UUID,

    @JsonProperty("order_id")
    @Schema(description = "UUID of the order being shipped", example = "123e4567-e89b-12d3-a456-426614174000")
    val orderId: UUID,

    @JsonProperty("merchant_shipment_id")
    @Schema(description = "Unique identifier for this shipment from the merchant", example = "SHIP-2025-001")
    val merchantShipmentId: String,

    @JsonProperty("courier")
    @Schema(description = "Name of the courier service used", example = "DHL")
    val courier: String,

    @JsonProperty("tracking_id")
    @Schema(description = "Tracking number provided by the courier", example = "DHL-123456789")
    val trackingId: String,

    @JsonProperty("shipped_amount")
    @Schema(description = "Amount shipped in this shipment")
    val shippedAmount: Amount,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("shipped_at")
    @Schema(description = "Date and time when the shipment was made (UTC)", example = "2025-08-11T15:30:00Z")
    val shippedAt: Instant,

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("created_at")
    @Schema(description = "Date and time when Billie received the shipment notification (UTC)", example = "2025-08-11T15:31:00Z")
    val createdAt: Instant
)
