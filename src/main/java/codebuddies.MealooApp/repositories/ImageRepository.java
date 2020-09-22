package codebuddies.MealooApp.repositories;

import codebuddies.MealooApp.entities.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    void deleteByFileUrl(String fileUrl);

    Optional<Image> findByFileUrl(String fileUrl);

}
