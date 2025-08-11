package io.billie.orders.viewmodel

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import io.swagger.v3.oas.annotations.media.Schema
import java.time.Instant
import java.time.LocalDateTime
import java.util.*

data class OrderResponse(
    @JsonProperty("id")
    @Schema(description = "Billie's internal order ID", example = "123e4567-e89b-12d3-a456-426614174000")
    val id: UUID,
    
    @JsonProperty("organization_id") 
    @Schema(description = "UUID of the merchant organization", example = "6dc7acb9-3fba-4c5b-bd0c-6898b6ec152a")
    val organizationId: UUID,
    
    @JsonProperty("merchant_order_id") 
    @Schema(description = "Merchant's internal order ID", example = "689a4d192c1d62d71289b03d")
    val merchantOrderId: String,
    
    @JsonProperty("buyer_id") 
    @Schema(description = "UUID of the buyer", example = "46aac123-9cee-4a5f-9986-386eb36bd70e")
    val buyerId: UUID,
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("order_date") 
    @Schema(description = "Date and time when the order was created in merchant's system (UTC)", example = "2025-08-11T14:30:00Z")
    val orderDate: Instant,
    
    @JsonProperty("total_amount") 
    @Schema(description = "Total amount of the order")
    val totalAmount: Amount,
    
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonProperty("created_at") 
    @Schema(description = "Date and time when Billie received the order notification (UTC)", example = "2025-08-11T14:31:15Z")
    val createdAt: Instant
)