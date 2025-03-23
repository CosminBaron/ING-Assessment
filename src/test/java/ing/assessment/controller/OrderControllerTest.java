package ing.assessment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ing.assessment.controller.OrderController;
import ing.assessment.db.order.Order;
import ing.assessment.dto.OrderDto;
import ing.assessment.service.OrderService;
import ing.assessment.validation.ValidatorErrorCodes;
import ing.assessment.validation.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private OrderService orderService;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testPlaceOrder_Success() throws Exception {
        OrderDto orderDto = new OrderDto();
        Order savedOrder = new Order();

        when(orderService.placeOrder(any(OrderDto.class))).thenReturn(savedOrder);

        mockMvc.perform(post("/orders/placeOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testGetOrder_Success() throws Exception {
        int orderId = 1;
        Order order = new Order();

        when(orderService.getOrderById(orderId)).thenReturn(order);

        mockMvc.perform(get("/orders/viewOrderDetails/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void testGetOrder_NotFound() throws Exception {
        int orderId = 999;
        when(orderService.getOrderById(anyInt())).thenThrow(new RuntimeException(ValidatorErrorCodes.ORDER_NOT_FOUND));

        mockMvc.perform(get("/orders/viewOrderDetails/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
