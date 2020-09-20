package codebuddies.MealooApp.dataproviders;

import codebuddies.MealooApp.repositories.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ImageProvider {

    private ModelMapper modelMapper;

    private ImageRepository imageRepository;

    public ImageProvider() {
    }

    public ImageProvider(ModelMapper modelMapper, ImageRepository imageRepository) {
        this.modelMapper = modelMapper;
        this.imageRepository = imageRepository;
    }

}
