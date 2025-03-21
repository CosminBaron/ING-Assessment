package ing.assessment.controller;

import ing.assessment.db.order.Order;
import ing.assessment.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @PostMapping("/placeOrder")
    public void placeOrder(@RequestBody Order order){
        this.orderService.placeOrder(order);
    }

    @GetMapping("/viewOrderDetails")
    public Optional<Order> getOrder(@PathVariable("id") Integer id) {
        return orderService.getOrderById(id);
    }


}
