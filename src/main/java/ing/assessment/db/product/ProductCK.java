package ing.assessment.db.product;

import ing.assessment.model.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductCK {
    private Integer id;
    private Location location;


    public ProductCK() {
    }

    public ProductCK(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public Location getLocation() {
        return location;
    }

}