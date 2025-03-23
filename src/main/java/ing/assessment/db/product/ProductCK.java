package ing.assessment.db.product;

import ing.assessment.model.Location;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Embeddable
public class ProductCK {
    private Integer id;
    private Location location;

    public ProductCK() {
    }

    public ProductCK(Integer id) {
        this.id = id;
    }

    public ProductCK(Integer id, Location location) {
        this.id = id;
        this.location = location;
    }

    public Integer getId() {
        return id;
    }
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


}