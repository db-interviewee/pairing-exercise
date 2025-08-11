package io.billie.shipments.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class CurrencyMismatchException(
    orderCurrency: String,
    shipmentCurrency: String
) : IllegalArgumentException("Currency mismatch: order is $orderCurrency, shipment is $shipmentCurrency")