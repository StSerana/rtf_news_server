package com.example.servingwebcontent;

import java.util.List;

import com.example.servingwebcontent.Article;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

    List<Article> findAllByMagazine(Magazine magazine);

    Article findById(long id);
}