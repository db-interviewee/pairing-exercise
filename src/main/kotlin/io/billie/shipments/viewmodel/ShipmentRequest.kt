package io.billie.shipments.viewmodel

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.billie.orders.viewmodel.Amount
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.*
import javax.validation.constraints.NotNull

data class ShipmentRequest(
    @field:NotNull
    @JsonProperty("order_id")
    @Schema(description = "UUID of the order being shipped", example = "123e4567-e89b-12d3-a456-426614174000")
    val orderId: UUID,

    @field:NotNull
    @JsonProperty("merchant_shipment_id")
    @Schema(description = "Unique identifier for this shipment from the merchant", example = "SHIP-2025-001")
    val merchantShipmentId: String,

    @field:NotNull
    @JsonProperty("courier")
    @Schema(description = "Name of the courier service used", example = "DHL")
    val courier: String,

    @field:NotNull
    @JsonProperty("tracking_id")
    @Schema(description = "Tracking number provided by the courier", example = "DHL-123456789")
    val trackingId: String,

    @field:NotNull
    @JsonProperty("shipped_amount")
    @Schema(description = "Amount being shipped in this shipment")
    val shippedAmount: Amount,

    @field:NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("shipped_at")
    @Schema(description = "Date and time when the shipment was made (UTC)", example = "2025-08-11T15:30:00Z")
    val shippedAt: Instant
)
