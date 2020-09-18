package codebuddies.MealooApp.services;

import codebuddies.MealooApp.entities.image.Image;
import codebuddies.MealooApp.repositories.ImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class ImageService {

    @Value("${cloudinary.cloudNameValue}")
    private String cloudNameValue;
    @Value("${cloudinary.apiKeyValue}")
    private String apiKeyValue;
    @Value("${cloudinary.apiSecretValue}")
    private String apiSecretValue;

    private ImageRepository imageRepository;

    //todo Add improvement here
    private Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudNameValue,
            "api_key", apiKeyValue,
            "api_secret", apiSecretValue));

    public ImageService(ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public Map addNewImage(String filePath) throws IOException {
        File file = new File(filePath);
        return cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
    }

    public Image save(Image image) {
        return imageRepository.save(image);
    }

    public void delete(Image image){
        imageRepository.delete(image);
    }

    public void deleteByFileUrl(String fileUrl) throws IOException {
        imageRepository.deleteByFileUrl(fileUrl);
    }

}
