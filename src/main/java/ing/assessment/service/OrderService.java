package ing.assessment.service;

import ing.assessment.db.order.Order;

import java.util.Optional;

public interface OrderService {
    void placeOrder(Order order);
    Optional<Order> getOrderById(Integer id);
}