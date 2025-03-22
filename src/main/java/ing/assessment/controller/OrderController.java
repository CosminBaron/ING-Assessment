package ing.assessment.controller;

import ing.assessment.db.order.Order;
import ing.assessment.dto.OrderDto;
import ing.assessment.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDto orderDto){
         Order savedOrder = orderService.placeOrder(orderDto);
        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
    }

    @GetMapping("/viewOrderDetails/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Integer id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }


}
