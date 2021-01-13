package com.bokudos.bokudosserver.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Profile(value = {"local", "dev"})
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Bokudos Game API",
                null,
                "0.0.1",
                null,
                null,
                null,
                null,
                Collections.emptyList());
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Bokudos Game API: V1")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bokudos.bokudosserver"))
                .paths(PathSelectors.any())
                .build();
    }
}
