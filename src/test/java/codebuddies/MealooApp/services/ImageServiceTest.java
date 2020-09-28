package codebuddies.MealooApp.services;

import codebuddies.MealooApp.datamappers.ImageMapper;
import codebuddies.MealooApp.dto.ImageDTO;
import codebuddies.MealooApp.exceptions.ResourceNotFoundException;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(SpringExtension.class)
class ImageServiceTest {

    @Mock
    ImageMapper imageMapper;
    @Mock
    Cloudinary cloudinary;

    ImageDTO image;

    ImageService imageService;


    @BeforeEach
    void setUp() {
        cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "cloudName",
                "api_key", "apiKey",
                "api_secret", "apiSecret"));
        image = new ImageDTO("fileUrl");
        imageService = new ImageService(imageMapper);
    }

    @Test
    void shouldReturnImageByUrlIfExists() {
        //given
        given(imageMapper.existsByFileUrl("fileUrl")).willReturn(true);
        given(imageMapper.getImageByFileUrl("fileUrl")).willReturn(image);

        //when
        ImageDTO result = imageService.getImageByFileUrl("fileUrl");

        //then
        assertThat(result.getFileUrl(), equalTo("fileUrl"));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionIfImageDoesNotExist() {
        //given + when
        given(imageMapper.existsByFileUrl("fileUrl")).willReturn(false);

        //then
        assertThrows(ResourceNotFoundException.class, () ->
                imageService.getImageByFileUrl("fileUrl"));
    }
}