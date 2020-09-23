package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.user.MealooUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MealooUserRepository extends JpaRepository<MealooUser, Long> {

    Optional<MealooUser> findByUsername(String username);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);
}
