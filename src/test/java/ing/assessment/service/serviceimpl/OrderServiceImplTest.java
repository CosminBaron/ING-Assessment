package ing.assessment.service.serviceimpl;

import ing.assessment.db.order.Order;
import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.db.repository.OrderRepository;
import ing.assessment.db.repository.ProductRepository;
import ing.assessment.model.Location;
import ing.assessment.service.impl.OrderServiceImpl;
import ing.assessment.validation.ValidatorException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
public class OrderServiceImplTest {
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final OrderServiceImpl orderService = new OrderServiceImpl(orderRepository, productRepository);


    @Test
    void testGetOrderById_WithValidId() {
        Order order = new Order();
        order.setId(1);
        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testGetOrderById_WithInvalidId() {
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(999));
    }


    @Test
    void testComputeCost_WithValidOrder() {
        List<OrderProduct> orderProducts = List.of(new OrderProduct(1, 2));
        Product product = new Product(new ProductCK(1, Location.MUNICH), "Shoes", 400.0, 50);

        when(productRepository.findAvailableProduct(1, 2)).thenReturn(List.of(product));

        doNothing().when(productRepository).updateProductStock(
                eq(1), eq(Location.MUNICH), eq(2)
        );

        double totalCost = orderService.computeCost(orderProducts);

        assertEquals(800.0, totalCost);
    }

    @Test
    void testComputeCost_WithDiscount() {
        List<OrderProduct> orderProducts = List.of(new OrderProduct(1, 11));
        Product product = new Product(new ProductCK(1, Location.MUNICH), "Shoes", 100.0, 50);

        when(productRepository.findAvailableProduct(1, 11)).thenReturn(List.of(product));

        doNothing().when(productRepository).updateProductStock(
                eq(1), eq(Location.MUNICH), eq(2)
        );

        double totalCost = orderService.computeCost(orderProducts);

        assertEquals(990.0, totalCost);
    }

    @Test
    void testComputeDeliveryCost_WithCostGreaterThan500() {
        int deliveryCost = orderService.computeDeliveryCost(600.0);

        assertEquals(0, deliveryCost);
    }

    @Test
    void testComputeDeliveryCost_WithCostLessThan500() {
        int deliveryCost = orderService.computeDeliveryCost(400.0);

        assertEquals(30, deliveryCost);
    }

    @Test
    void testComputeDeliveryTime_WithValidOrder() {
        OrderProduct orderProduct = new OrderProduct(1, 2);
        List<OrderProduct> orderProducts = List.of(orderProduct);

        Product product = new Product();
        product.setQuantity(50);
        product.setProductCk(new ProductCK(1, Location.MUNICH));

        when(productRepository.findByProductCk_Id(anyInt())).thenReturn(List.of(product));

        int deliveryTime = orderService.computeDeliveryTime(orderProducts);

        assertEquals(2, deliveryTime);
    }
}
