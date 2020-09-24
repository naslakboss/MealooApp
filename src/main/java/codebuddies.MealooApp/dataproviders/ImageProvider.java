package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.entities.meal.Meal;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;
import codebuddies.MealooApp.repositories.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ImageProvider {

    private ModelMapper modelMapper;

    private ImageRepository imageRepository;

    public ImageProvider(ModelMapper modelMapper, ImageRepository imageRepository) {
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
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
}
