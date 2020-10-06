package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.user.MealooUserRole;
import codebuddies.MealooApp.entities.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(MealooUserRole name);
}
