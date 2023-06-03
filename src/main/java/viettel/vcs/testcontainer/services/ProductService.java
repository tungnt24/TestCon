package viettel.vcs.testcontainer.services;

import java.util.List;
import viettel.vcs.testcontainer.entities.Product;

public interface ProductService {
    List<Product> findAllProducts();
    Product addProduct(String name);
}
