package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.classes.Image;
import com.example.servingwebcontent.classes.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<Image, Long> {
    List<Image> findAllByPage(Page page);
}
