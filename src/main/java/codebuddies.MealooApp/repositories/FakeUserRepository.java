package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.user.FakeUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FakeUserRepository extends JpaRepository<FakeUser, Long> {
    FakeUser findByUsername(String username);

}
