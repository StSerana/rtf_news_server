package com.example.servingwebcontent.services;

import com.example.servingwebcontent.repository.ImageRepository;
import com.example.servingwebcontent.repository.PageRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Base64;

@Service
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final PageRepository pageRepository;

    @Value("${imagesFolder}")
    private String PATH_TO_IMAGES;

    public ImageService(ImageRepository imageRepository, PageRepository pageRepository) {
        this.imageRepository = imageRepository;
        this.pageRepository = pageRepository;
    }

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

    public String uploadImage(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(PATH_TO_IMAGES + file.getOriginalFilename())));
            stream.write(bytes);
            stream.close();
            return file.getOriginalFilename();
        } catch (IOException ex){
            log.error("You have problems with images !" + ex.getLocalizedMessage());
            log.error(ex.toString());
            return "null.png";
        }
    }

}
