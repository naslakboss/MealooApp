package codebuddies.MealooApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {


    private String TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtZWFsb29hZG1pbiIsImlhdCI6MTYwMjMxMDI4MiwiZXhwIjoxNjAzMTc0MjgyfQ.8Cmu_wE1bIZssgRiGZhfs39WGHhOE3E7qeYw6o1trT9y4eBc84PwM5Pq3g9NYoP4eTAsCXl7gRn6kTPFMuAJVg";

    @Bean
    public Docket docket() {

        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any()).build()
                .useDefaultResponseMessages(false).apiInfo(apiInfo())
                .globalOperationParameters(Collections.singletonList(
                    new ParameterBuilder()
                        .name("Authorization")
                        .modelRef(new ModelRef("string"))
                        .parameterType("header")
                        .required(true)
                        .hidden(true)
                        .defaultValue("Bearer " + TOKEN)
                        .build()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Mealoo App").version("1.0.0").build();
    }
}

