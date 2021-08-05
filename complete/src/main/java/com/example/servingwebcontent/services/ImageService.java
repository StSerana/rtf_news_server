package com.example.servingwebcontent.services;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Base64;
import java.util.stream.Stream;

@Service
public class ImageService {

    @Value("${imagesFolder}")
    private String PATH_TO_IMAGES;

    public String load(String filename) throws IOException {
        File file = new File(PATH_TO_IMAGES + filename);
        //InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PATH_TO_IMAGES + filename);
        if (file == null) {
            throw new IllegalArgumentException("file not found! " + PATH_TO_IMAGES + filename);
        } else {
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            return encodedString;
        }
    }

}
