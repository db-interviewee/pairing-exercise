package io.billie.functional

import io.billie.functional.data.Fixtures.orderRequest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsEqual.equalTo
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = DEFINED_PORT)
class CanNotifyOrderTest {

    @LocalServerPort
    private val port = 8080

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun canPostAnOrder() {
        val postResult = mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(UUID.randomUUID()))
        )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andReturn()
        
        val location = postResult.response.getHeader("Location")!!

        mockMvc.perform(
            get(location)
                .accept(APPLICATION_JSON)
        )
            .andExpect(status().isOk())
    }

    @Test
    fun canPostSameOrderTwiceIdempotently() {
        val merchantOrderId = UUID.randomUUID()

        val firstResponse = mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(merchantOrderId))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .response
            .contentAsString

        val secondResponse = mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(merchantOrderId))
        )
            .andExpect(status().isCreated())
            .andReturn()
            .response
            .contentAsString

        assertThat(secondResponse, equalTo(firstResponse))
    }

    @Test
    fun cannotPostSameOrderIdWithDifferentAmount() {
        val merchantOrderId = UUID.randomUUID()

        mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(merchantOrderId, 10000))
        )
            .andExpect(status().isCreated())

        mockMvc.perform(
            post("/orders")
                .contentType(APPLICATION_JSON)
                .content(orderRequest(merchantOrderId, 20000))
        )
            .andExpect(status().isConflict())
    }
}
