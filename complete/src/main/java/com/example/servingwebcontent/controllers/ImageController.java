package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.services.ImageService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;

@RestController
@CrossOrigin
public class ImageController {
    @Autowired
    private final ImageService imageService;

    @Value("${imagesFolder}")
    private String PATH_TO_IMAGES;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @RequestMapping(value = "/images", method = RequestMethod.GET)
    public @ResponseBody String serveFile(@RequestParam(name="filename", required=false, defaultValue="aaa.png") String filename) throws Exception {
        /*InputStream file = imageService.load(filename);
        return IOUtils.toByteArray(file);*/
        return imageService.load(filename);
    }

    @RequestMapping(value = "/upload_image", method = RequestMethod.POST)
    public @ResponseBody String uploadImage(@RequestParam("image") MultipartFile file) {
        String imageName = imageService.uploadImage(file);
        return imageName;
    }
}
