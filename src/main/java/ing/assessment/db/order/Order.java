package ing.assessment.db.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date timestamp;
    @ElementCollection
    private List<OrderProduct> orderProducts;
    private Double orderCost;
    private Integer deliveryCost = 30; // Default cost of the order
    private Integer deliveryTime = 2;  // Default delivery time for the order

    public Integer getId() {return id;}

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public Double getOrderCost() {return orderCost;}

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public void setOrderCost(Double orderCost) {
        this.orderCost = orderCost;
    }

    public void setDeliveryCost(Integer deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}