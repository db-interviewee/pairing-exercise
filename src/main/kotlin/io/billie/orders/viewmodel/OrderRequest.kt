package io.billie.orders.viewmodel

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class OrderRequest(
    @field:NotNull
    @JsonProperty("merchant_id")
    @Schema(description = "UUID of the merchant organization placing the order", example = "6dc7acb9-3fba-4c5b-bd0c-6898b6ec152a")
    val merchantId: UUID,

    @field:NotBlank
    @JsonProperty("merchant_order_id")
    @Schema(description = "Merchant's internal order ID for idempotency", example = "689a4d192c1d62d71289b03d")
    val merchantOrderId: String,

    @field:NotNull
    @JsonProperty("buyer_id")
    @Schema(description = "UUID of the buyer", example = "46aac123-9cee-4a5f-9986-386eb36bd70e")
    val buyerId: UUID,

    @field:NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("order_date")
    @Schema(description = "Date and time when the order was created in merchant's system (UTC)", example = "2025-08-11T14:30:00Z")
    val orderDate: Instant,

    @field:NotNull
    @JsonProperty("total_amount")
    @Schema(description = "Total amount of the order")
    val totalAmount: Amount,
)

