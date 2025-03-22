package ing.assessment.dto;

import ing.assessment.db.order.OrderProduct;

import java.util.List;

public class OrderDto {
    private List<OrderProduct> orderProducts;

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }
}
