package com.shopme.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // map the path of the request (URI) to
        // the absolute path of the directory on the server.

        // user
        String dirName = "user-photos";
        Path userPhotosDir = Paths.get(dirName);

        // getAbsolutePath(): get the original link of the project
        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + dirName + "/**")
                .addResourceLocations("file:/" + userPhotosPath + "/");


        // category
        String categoryImagesDirName = "../category-images";
        Path categoryImagesDir = Paths.get(categoryImagesDirName);

        String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/category-images/**")
                .addResourceLocations("file:/" + categoryImagesPath + "/");

    }

}
