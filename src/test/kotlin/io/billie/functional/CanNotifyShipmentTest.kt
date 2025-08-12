package io.billie.functional

import com.fasterxml.jackson.databind.ObjectMapper
import io.billie.functional.data.Fixtures.orderRequest
import io.billie.functional.data.Fixtures.shipmentRequest
import io.billie.orders.viewmodel.OrderResponse
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanNotifyShipmentTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var mapper: ObjectMapper

    @Test
    fun canShipPartOfOrder() {
        val orderResponse = mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(UUID.randomUUID()))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .response
            .contentAsString

        val order = mapper.readValue(orderResponse, OrderResponse::class.java)

        val merchantShipmentId = "SHIP-001"
        mockMvc.perform(
            post("/shipments")
                .contentType(APPLICATION_JSON)
                .content(shipmentRequest(order.id, 6000, merchantShipmentId, "DHL", "DHL-123456"))
        )
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.order_id").value(order.id.toString()))
            .andExpect(jsonPath("$.merchant_shipment_id").value(merchantShipmentId))
            .andExpect(jsonPath("$.courier").value("DHL"))
            .andExpect(jsonPath("$.tracking_id").value("DHL-123456"))
            .andExpect(jsonPath("$.shipped_amount.amount").value(6000))
    }

    @Test
    fun cannotShipMoreThanOrderTotal() {
        val orderResponse = mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(UUID.randomUUID()))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .response
            .contentAsString

        val order = mapper.readValue(orderResponse, OrderResponse::class.java)

        // Try to ship more than order total
        mockMvc.perform(
            post("/shipments")
                .contentType(APPLICATION_JSON)
                .content(shipmentRequest(order.id, 15000))
        )
            .andExpect(status().isBadRequest())
    }

    @Test
    fun canShipInMultipleShipments() {
        val orderResponse = mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(UUID.randomUUID()))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .response
            .contentAsString

        val order = mapper.readValue(orderResponse, OrderResponse::class.java)

        mockMvc.perform(
            post("/shipments")
                .contentType(APPLICATION_JSON)
                .content(shipmentRequest(order.id, 3000))
        )
            .andExpect(status().isCreated())

        mockMvc.perform(
            post("/shipments")
                .contentType(APPLICATION_JSON)
                .content(shipmentRequest(order.id, 4000))
        )
            .andExpect(status().isCreated())

        // Third shipment would exceed the total
        mockMvc.perform(
            post("/shipments")
                .contentType(APPLICATION_JSON)
                .content(shipmentRequest(order.id, 4000))
        )
            .andExpect(status().isBadRequest())

        mockMvc.perform(
            get("/shipments/order/${order.id}")
                .accept(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
    }
}
