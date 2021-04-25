package com.example.servingwebcontent.repository;

import java.util.List;

import com.example.servingwebcontent.classes.Article;
import com.example.servingwebcontent.classes.Magazine;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

    List<Article> findAllByMagazine(Magazine magazine);
    Article findById(long id);
}