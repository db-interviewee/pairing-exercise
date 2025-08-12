package io.billie.orders.viewmodel

import io.swagger.v3.oas.annotations.media.Schema
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class Amount(
    @field:NotBlank 
    @field:Pattern(regexp = "EUR", message = "Only EUR currency is supported")
    @Schema(description = "ISO 4217 Currency code (currently only EUR is supported)", example = "EUR")
    val currency: String,
    
    @field:NotNull 
    @Schema(description = "Amount in minor units (e.g. cents)", example = "10000")
    val amount: Int,
    
    @field:NotNull 
    @field:Min(2) @field:Max(2)
    @Schema(description = "Number of decimal places (currently only 2 is supported)", example = "2")
    val decimal: Int
)