package codebuddies.MealooApp.services;

import codebuddies.MealooApp.repositories.ImageRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class ImageServiceTest {

    @Mock
    ImageRepository imageRepository;

    Cloudinary cloudinary;

    ImageService imageService;

    @BeforeEach
    void setUp(){

        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "codebuddies",
                "api_key", "568422233866872",
                "api_secret", "Ux0UcCPbF-yjojxpiaH5dEpFXUQ"));

        imageService = new ImageService(imageRepository);
    }
    @Test
    void addNewImage() throws IOException {
        //given
        String filePath = "filePath";
        File file = new File(filePath);
        //when
        Map result = imageService.addNewImage(filePath);
        //then
        verify(cloudinary.uploader(), times(1)).upload(file, ObjectUtils.emptyMap());
    }

    @Test
    void deleteByFileUrl() {
    }
}