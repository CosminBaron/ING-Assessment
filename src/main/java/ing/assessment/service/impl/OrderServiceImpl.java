package ing.assessment.service.impl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.dto.OrderDto;
import ing.assessment.model.Location;
import ing.assessment.service.OrderService;
import ing.assessment.validation.OutOfStockError;
import ing.assessment.validation.Validator;
import ing.assessment.validation.ValidatorErrorCodes;
import ing.assessment.validation.ValidatorException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Order getOrderById(Integer id) {
        return orderRepository.findById(id).orElseThrow(() -> new ValidatorException(ValidatorErrorCodes.CAN_NOT_FOUND_ORDER_WITH_ID + id));
    }

    @Override
    public Order placeOrder(OrderDto orderDto) {
        Order order = new Order();

        double totalCost = computeCost(orderDto.getOrderProducts());
        int deliveryCost = computeDeliveryCost(totalCost);
        int deliveryTime = computeDeliveryTime(orderDto.getOrderProducts());

        Validator.of(orderDto)
                .validate(orderDto.getOrderProducts() == null || orderDto.getOrderProducts().isEmpty(), ValidatorErrorCodes.ORDER_MUST_CONTAIN_AT_LEAST_ONE_PRODUCT)
                .validate(totalCost < 0, ValidatorErrorCodes.ORDER_COST_MUST_BE_GREATER_THAN_ZERO)
                .validate(deliveryCost < 0, ValidatorErrorCodes.INVALID_DELIVERY_COST)
                .validate(deliveryTime <= 0, ValidatorErrorCodes.DELIVERY_TIME_MUST_BE_AT_LEAST_TWO_DAYS)
                .get();


        order.setOrderProducts(orderDto.getOrderProducts());
        order.setTimestamp(new Date());
        order.setOrderCost(totalCost);
        order.setDeliveryCost(deliveryCost);
        order.setDeliveryTime(deliveryTime);


        return this.orderRepository.save(order);
    }

    public List<Product> findProductsById(List<OrderProduct> orderProducts) {
        var selectedProducts = new ArrayList<Product>();

        for (OrderProduct orderProduct : orderProducts) {
            if(orderProduct != null && orderProduct.getProductId() !=null && orderProduct.getQuantity() !=null ){
            List<Product> availableProducts = productRepository.findAvailableProduct(
                    orderProduct.getProductId(), orderProduct.getQuantity()
            );

            if (!availableProducts.isEmpty()) {
                Product selectedProduct = availableProducts.get(0);
                selectedProducts.add(selectedProduct);

                productRepository.updateProductStock(
                        selectedProduct.getProductCk().getId(),
                        selectedProduct.getProductCk().getLocation(),
                        orderProduct.getQuantity()
                );
            } else {
                throw new ValidatorException(ValidatorErrorCodes.ONE_OR_MORE_PRODUCTS_NOT_FOUND_IN_DATABASE);
            }
          } else{
                throw new ValidatorException(ValidatorErrorCodes.PRODUCT_SHOULD_HAVE_ID_AND_QUANTITY);
            }
        }

        return selectedProducts;
    }

    public double computeCost(List<OrderProduct> orderProducts){
        List<Product> products = findProductsById(orderProducts);
        Set<Integer> duplicateProduct = new HashSet<>();

        Validator.of(products)
                .validate(products.stream().anyMatch(p -> p.getPrice() <= 0),
                        ValidatorErrorCodes.PRODUCT_PRICE_MUST_BE_GREATER_THAN_ZERO)
                .validate(products.size() != orderProducts.size(), ValidatorErrorCodes.ONE_OR_MORE_PRODUCTS_NOT_FOUND_IN_DATABASE)
                .get();

        for (OrderProduct op : orderProducts) {
            if (!duplicateProduct.add(op.getProductId())) {
                throw new ValidatorException(ValidatorErrorCodes.DUPLICATE_PRODUCT_ID_FOUND + op.getProductId());
            }
        }

        Map<Integer, Double>  productsPriceMap = products.stream().collect(Collectors.toMap(product -> product.getProductCk().getId(), Product::getPrice));

        double totalCost = 0.0;
        for(OrderProduct orderProduct : orderProducts){
            totalCost += productsPriceMap.getOrDefault(orderProduct.getProductId(), 0.0) * orderProduct.getQuantity();
        }

        if (totalCost > 1000){
            totalCost *= 0.9;
        }

         return totalCost;
    }

    public int computeDeliveryCost(double totalCost){
        return totalCost > 500 ? 0 : 30;
    }


    public int computeDeliveryTime(List<OrderProduct> orderProducts) {
        Set<Location> uniqueLocations = new HashSet<>();

        Validator.of(orderProducts)
                .validate(orderProducts.stream().anyMatch(op -> op.getQuantity() <= 0),
                        ValidatorErrorCodes.PRODUCT_QUANTITY_MUST_BE_GREATER_THAN_ZERO)

                .get();

        for(OrderProduct orderProduct : orderProducts){
            int orderedQuantity = orderProduct.getQuantity();

            List<Product> products = productRepository.findByProductCk_Id(orderProduct.getProductId())
                    .stream()
                    .sorted(Comparator.comparingInt(Product::getQuantity))
                    .toList();

            if (products.isEmpty()) {
                throw new ValidatorException(ValidatorErrorCodes.CAN_NOT_FOUND_PRODUCT_WITH_ID + orderProduct.getProductId());
            }

            int stockProduct = products.stream().mapToInt(Product::getQuantity).sum();

            if (orderedQuantity > stockProduct) {
                throw new OutOfStockError(ValidatorErrorCodes.INSUFFICIENT_STOCK_FOR_PRODUCT_ID + orderProduct.getProductId());
            }


            if(orderedQuantity == 0){
                break;
            }

            for(Product product : products){
                if(orderedQuantity > 0  && product.getQuantity() > 0){
                    int quantityOutOfStock = Math.min(product.getQuantity(), orderedQuantity);
                    orderedQuantity -= quantityOutOfStock;
                    uniqueLocations.add(product.getProductCk().getLocation());
                }

            }
        }


        return uniqueLocations.isEmpty() ? 2 : uniqueLocations.size() * 2;
    }

    @Override
    public void deleteOrder(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ValidatorException(ValidatorErrorCodes.CAN_NOT_FOUND_ORDER_WITH_ID + orderId));

        long timeElapsed = (new Date().getTime() - order.getTimestamp().getTime()) / 1000;

        if (timeElapsed > 120) {
            throw new ValidatorException(ValidatorErrorCodes.ORDER_CANNOT_BE_CANCELLED);
        }

        orderRepository.delete(order);
    }

}