package viettel.vcs.testcontainer.containers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import viettel.vcs.testcontainer.entities.Product;
import viettel.vcs.testcontainer.repo.ProductRepository;

@DataJpaTest
@ActiveProfiles("postgres")
public class ProductRefactoredTest extends DatabaseTest{
    @Autowired
    private ProductRepository productRepository;

    @Test
    void productTest() {

        productRepository.save(new Product("hiha"));
        List<Product> list = productRepository.findAll();
        assertEquals(1, list.size());
    }
}
