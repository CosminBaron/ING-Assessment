package ing.assessment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ing.assessment.db.order.Order;
import ing.assessment.dto.OrderDto;
import ing.assessment.service.OrderService;
import ing.assessment.validation.InvalidOrderError;
import ing.assessment.validation.ValidatorErrorCodes;
import ing.assessment.validation.ValidatorException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    void testPlaceOrder_InvalidOrder() throws Exception {
        OrderDto invalidOrder = new OrderDto();
        when(orderService.placeOrder(any(OrderDto.class)))
                .thenThrow(new InvalidOrderError(ValidatorErrorCodes.INVALID_ORDER));

        mockMvc.perform(post("/orders/placeOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalidOrder)))
                .andExpect(status().isBadRequest());
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
        when(orderService.getOrderById(anyInt())).thenThrow(new ValidatorException(ValidatorErrorCodes.ORDER_NOT_FOUND));

        mockMvc.perform(get("/orders/viewOrderDetails/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteOrder_Success() throws Exception {
        doNothing().when(orderService).deleteOrder(1);

        mockMvc.perform(delete("/orders/deleteOrder/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void testDeleteOrder_ThrowsValidatorException() throws Exception {
        doThrow(new InvalidOrderError(ValidatorErrorCodes.INVALID_ORDER)).when(orderService).deleteOrder(2);

        mockMvc.perform(delete("/orders/deleteOrder/2"))
                .andExpect(status().isBadRequest());
    }

}
