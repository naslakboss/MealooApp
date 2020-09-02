package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.repositories.ImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageService {


    private ImageRepository imageRepository;

    //todo Add improvement here
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
    ));

    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public String addNewImage(String filePath) throws IOException {
        File file = new File(filePath);
        Map result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        return result.get("url").toString();
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public void delete(Image image){
        imageRepository.delete(image);
    }
}
