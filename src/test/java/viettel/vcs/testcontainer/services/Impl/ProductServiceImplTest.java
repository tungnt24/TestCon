package viettel.vcs.testcontainer.services.Impl;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.junit.jupiter.api.Assertions.*;

import viettel.vcs.testcontainer.containers.TestElasticsearchContainer;
import viettel.vcs.testcontainer.entities.Product;
import viettel.vcs.testcontainer.repo.ProductRepository;
import viettel.vcs.testcontainer.services.ProductService;

@Testcontainers
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceImplTest {

    private ProductService productService;
    @Autowired
    public void setClientRepository(ProductService productService){
        this.productService = productService;
    }
    @Autowired
    ElasticsearchRestTemplate elasticsearchTemplate;

    private Long productId;
    @Container
    private static ElasticsearchContainer elasticsearchContainer = new ElasticsearchContainer();

    @BeforeAll
    static void setUp() {
        elasticsearchContainer.start();
    }

    @BeforeEach
    void testIsContainerRunning() {
        assertTrue(elasticsearchContainer.isRunning());
        recreateIndex();
    }

    @Test
    void testCreateProduct(){
        Product product = productService.addProduct("random");
        productId = product.getId();
        assertNotNull(product);
        assertNotNull(product.getId());

    }
    private void recreateIndex() {
        if (elasticsearchTemplate.exists(String.valueOf(productId), Product.class)) {
            elasticsearchTemplate.delete(Product.class);
            elasticsearchTemplate.save(Product.class);
        }
    }
    @AfterAll
    static void destroy() {
        elasticsearchContainer.stop();
    }
}
