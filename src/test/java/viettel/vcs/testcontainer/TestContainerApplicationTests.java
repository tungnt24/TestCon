package viettel.vcs.testcontainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import viettel.vcs.testcontainer.entities.Product;
import viettel.vcs.testcontainer.repo.ProductRepository;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = TestContainerApplicationTests.DataSourceInitializer.class)
class TestContainerApplicationTests {
    @Container
    private static final PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres:12.9-alpine");
    @Autowired
    private ProductRepository productRepository;

    @Test
    void mainTest() {

        List<Product> list = productRepository.findAll();
        assertEquals(0, list.size());
    }
    public static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.test.database.replace=none",
                "spring.datasource.url=" + database.getJdbcUrl(),
                "spring.datasource.username=" + database.getUsername(),
                "spring.datasource.password=" + database.getPassword()
            );
        }
    }
}
