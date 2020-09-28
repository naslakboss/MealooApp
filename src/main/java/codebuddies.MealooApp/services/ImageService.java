package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.ImageMapper;
import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.dto.MealDTO;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;

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

    Cloudinary cloudinary = new Cloudinary(ObjectUtils.emptyMap());

    private final ImageMapper imageMapper;

    public ImageService(ImageMapper imageMapper) {
        this.imageMapper = imageMapper;
    }

    private boolean existsByFileUrl(String fileUrl) {
        return imageMapper.existsByFileUrl(fileUrl);
    }

    public ImageDTO getImageByFileUrl(String fileUrl) {
        if (!existsByFileUrl(fileUrl)) {
            throw new ResourceNotFoundException(fileUrl);
        }
        return imageMapper.getImageByFileUrl(fileUrl);
    }

    public void createNewImage(MealDTO mealDTO, String filePath) {
        String imageUrl = uploadFile(filePath);
        imageMapper.createNewImage(filePath, imageUrl, mealDTO);
    }

    public String uploadFile(String filePath) {
        File file = new File(filePath);
        Map result = null;
        try {
            cloudinary.config.cloudName = cloudNameValue;
            cloudinary.config.apiKey = apiKeyValue;
            cloudinary.config.apiSecret = apiSecretValue;
            result = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result != null) {
            return result.get("url").toString();
        }
        else throw new RuntimeException("Image could not be uploaded");
    }
}
