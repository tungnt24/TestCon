package viettel.vcs.testcontainer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import viettel.vcs.testcontainer.entities.Product;
import viettel.vcs.testcontainer.services.Impl.ProductServiceImpl;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductServiceImpl productService;

    @PostMapping("/add")
    public Product createAccount(@RequestParam String name) {
        return productService.addProduct(name);
    }
}
