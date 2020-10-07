package codebuddies.MealooApp.datamappers;

import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.ImageRepository;

import org.modelmapper.ModelMapper;

import org.springframework.stereotype.Service;

@Service
public class ImageMapper {

    private ModelMapper modelMapper = new ModelMapper();

    private ImageRepository imageRepository;

    public ImageMapper(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public boolean existsByFileUrl(String fileUrl) {
        return imageRepository.existsByFileUrl(fileUrl);
    }

    public void createNewImage(String filePath, String imageUrl, MealDTO mealDTO) {
        Meal meal = modelMapper.map(mealDTO, Meal.class);
        Image image = new Image(filePath, imageUrl, meal);
        imageRepository.save(image);
    }

    public ImageDTO getImageByFileUrl(String fileUrl) {
        Image image = imageRepository.findByFileUrl(fileUrl).orElseThrow(() ->
                new ResourceNotFoundException(fileUrl));

        return modelMapper.map(image, ImageDTO.class);
    }

    public void deleteImageByFileUrl(String fileUrl) {
         imageRepository.deleteImageByFileUrl(fileUrl);
    }
}
