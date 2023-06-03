package viettel.vcs.testcontainer.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import viettel.vcs.testcontainer.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}