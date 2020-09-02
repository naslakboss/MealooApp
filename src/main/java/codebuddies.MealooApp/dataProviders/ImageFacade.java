package codebuddies.MealooApp.dataProviders;

import codebuddies.MealooApp.repositories.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ImageFacade {

    private ModelMapper modelMapper;

    private ImageRepository imageRepository;

    public ImageFacade() {
    }

    public ImageFacade(ModelMapper modelMapper, ImageRepository imageRepository) {
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }

}
