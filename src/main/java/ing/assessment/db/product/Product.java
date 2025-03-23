package ing.assessment.db.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
//@NoArgsConstructor
//@AllArgsConstructor
public class Product {
    @EmbeddedId
    private ProductCK productCk;

    private String name;
    private Double price;
    private Integer quantity;
    public Product() {
    }
    public Product(ProductCK productCk, String name, Double price, Integer quantity) {
        this.productCk = productCk;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public ProductCK getProductCk() {
        return productCk;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setProductCk(ProductCK productCk) {this.productCk = productCk;}

    public void setPrice(Double price) {this.price = price;}

    public void setQuantity(Integer quantity) {this.quantity = quantity;}

}