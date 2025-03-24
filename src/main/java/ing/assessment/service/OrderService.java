package ing.assessment.service;

import ing.assessment.db.order.Order;
import ing.assessment.dto.OrderDto;

import java.util.Optional;

public interface OrderService {
    Order placeOrder(OrderDto orderDto);
    Order getOrderById(Integer id);
    void deleteOrder(Integer orderId);
}