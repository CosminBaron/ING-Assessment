package ing.assessment.db.repository;

import ing.assessment.db.product.Product;
import ing.assessment.db.product.ProductCK;
import ing.assessment.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductCK> {
    List<Product> findByProductCk_Id(Integer id);

    @Query("SELECT p FROM Product p WHERE p.productCk.id = :id AND p.quantity >= :quantity ORDER BY p.productCk.location ASC")
    List<Product> findAvailableProduct(@Param("id") Integer id, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE Product p SET p.quantity = p.quantity - :quantity WHERE p.productCk.id = :id AND p.productCk.location = :location")
    void updateProductStock(@Param("id") Integer id, @Param("location") Location location, @Param("quantity") Integer quantity);
}