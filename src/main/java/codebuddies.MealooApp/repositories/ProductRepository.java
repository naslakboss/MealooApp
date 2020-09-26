package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.product.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Optional<Product> findByName(String name);

    boolean existsByName(String name);

    void deleteByName(String name);
}
