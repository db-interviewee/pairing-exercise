package io.billie.orders.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.util.*

@ResponseStatus(HttpStatus.NOT_FOUND)
class OrderNotFoundException(orderId: UUID) : RuntimeException("Order not found: $orderId")