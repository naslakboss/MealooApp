package codebuddies.MealooApp.services;

import codebuddies.MealooApp.dataproviders.ImageProvider;
import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.dto.MealDTO;
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

    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", cloudNameValue,
            "api_key", apiKeyValue,
            "api_secret", apiSecretValue));

    private ImageProvider imageProvider;

    public ImageService(ImageProvider imageProvider){
        this.imageProvider = imageProvider;
    }

    public void createNewImage(MealDTO mealDTO, String filePath) {
        String imageUrl = uploadFile(filePath);
        imageProvider.createNewImage(filePath, imageUrl, mealDTO);
    }

    public String uploadFile(String filePath) {
        File file = new File(filePath);
        Map result = null;
        try{
            //todo rework here
            cloudinary.config.cloudName = cloudNameValue;
            cloudinary.config.apiKey = apiKeyValue;
            cloudinary.config.apiSecret = apiSecretValue;
            result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        } catch (IOException e){
            e.printStackTrace();
        }
        return result.get("url").toString();
    }

    public ImageDTO getImageByFileUrl(String fileUrl){
        return imageProvider.getImageByFileUrl(fileUrl);
    }

}
