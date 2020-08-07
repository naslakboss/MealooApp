package codebuddies.MealooApp.repositories;


import codebuddies.MealooApp.entities.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends MongoRepository<ProductType, String>{

    @Override
    boolean existsById(String name);

    @Override
    void deleteById(String name);
}
