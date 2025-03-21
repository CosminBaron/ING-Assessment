package ing.assessment.service;

import ing.assessment.db.order.OrderProduct;
import ing.assessment.db.product.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    List<Product> getProductsById(Integer id);

    List<OrderProduct> checkProductStock (List<OrderProduct> orderProducts);
}