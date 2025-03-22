package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.dto.OrderDto;
import ing.assessment.model.Location;
import ing.assessment.service.OrderService;
import ing.assessment.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order with ID" + id + " not found"));
    }

    @Override
    public Order placeOrder(OrderDto orderDto) {
        Order order = new Order();
        order.setOrderProducts(orderDto.getOrderProducts());
        order.setTimestamp(new Date());

        double totalCost = computeCost(orderDto.getOrderProducts());
        order.setOrderCost(totalCost);

        int deliveryCost = computeDeliveryCost(totalCost);
        order.setDeliveryCost(deliveryCost);

        int deliveryTime = computeDeliveryTime(orderDto.getOrderProducts());
        order.setDeliveryTime(deliveryTime);



        return this.orderRepository.save(order);
    }

    private List<Product> findProductsById(List<OrderProduct> orderProducts) {
        List<Integer> productIds = orderProducts.stream().map(OrderProduct::getProductId).toList();
        List<ProductCK> productCKS = productIds.stream().map(ProductCK::new).toList();
        return productRepository.findAllById(productCKS);
    }

    private double computeCost(List<OrderProduct> orderProducts){
        List<Product> products = findProductsById(orderProducts);
        Map<Integer, Double>  productsPriceMap = products.stream().collect(Collectors.toMap(product -> product.getProductCk().getId(), Product::getPrice));

        double totalCost = 0.0;
        for(Map.Entry<Integer, Double> mapEntry : productsPriceMap.entrySet()){
            totalCost += productsPriceMap.getOrDefault(mapEntry.getKey(), 0.0) * mapEntry.getValue();
        }

        if (totalCost > 1000){
            totalCost *= 0.1;
        }

         return  totalCost;
    }

    private int computeDeliveryCost(double totalCost){
        return totalCost > 500 ? 0 : 30;
    }

    private int computeDeliveryTime(List<OrderProduct> orderProducts) {
        Set<Location> uniqueLocations = new HashSet<>();

        for(OrderProduct orderProduct : orderProducts){
            int orderedQuantity = orderProduct.getQuantity();

            List<Product> products = productRepository.findByProductCk_Id(orderProduct.getProductId());

            if(orderedQuantity == 0){
                break;
            }

            for(Product product : products){
                if(product.getQuantity() > 0){
                    int quantityOutOfStock = Math.min(product.getQuantity(), orderedQuantity);
                    orderedQuantity -= quantityOutOfStock;
                    uniqueLocations.add(product.getProductCk().getLocation());
                }

            }
        }


        return uniqueLocations.isEmpty() ? 2 : uniqueLocations.size() * 2;
    }


}