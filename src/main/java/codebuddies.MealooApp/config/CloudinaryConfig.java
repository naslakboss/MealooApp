package codebuddies.MealooApp.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//todo create configuration to reduce code in services
//@Configuration
//public class CloudinaryConfig {
//    @Value("${cloudinary.cloudNameValue}")
//    private static String cloudNameValue;
//    @Value("${cloudinary.apiKeyValue}")
//    private static String apiKeyValue;
//    @Value("${cloudinary.apiSecretValue}")
//    private static String apiSecretValue;
//
//    @Bean
//    public static Cloudinary cloudinary(){
//        return new Cloudinary(ObjectUtils.asMap("cloud_name", cloudNameValue, "api_key", apiKeyValue, "api_secret", apiSecretValue));
//    }

//    @Bean
//    public ImageUploader imageUtils(Cloudinary cloudinary){
//        return new ImageUploader(cloudinary);
//    }
//}

