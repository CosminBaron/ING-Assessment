package ing.assessment.controller;

import ing.assessment.db.order.Order;
import ing.assessment.dto.OrderDto;
import ing.assessment.service.OrderService;
import ing.assessment.validation.ValidatorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {this.orderService = orderService;}

    @PostMapping("/placeOrder")
    public ResponseEntity<Order> placeOrder(@RequestBody OrderDto orderDto){
        try{
            Order savedOrder = orderService.placeOrder(orderDto);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } catch (ValidatorException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/viewOrderDetails/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable("id") Integer id) {
        try{
            Order order = orderService.getOrderById(id);
            return ResponseEntity.ok(order);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/deleteOrder/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") Integer id) {
        try{
            orderService.deleteOrder(id);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (ValidatorException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
}
