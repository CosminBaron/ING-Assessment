package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.service.OrderService;
import ing.assessment.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductService productService;

    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Override
    public Optional<Order> getOrderById(Integer id) {
        Optional<Order> order = orderRepository.findById(id);
        return order;
    }

    @Override
    public void placeOrder(Order order) {
        List<OrderProduct> orderProductsOutOfStock = new ArrayList<>(productService.checkProductStock(order.getOrderProducts()));

        if(orderProductsOutOfStock.isEmpty()){
            this.orderRepository.save(order);
        }

    }
}