package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.classes.Article;
import com.example.servingwebcontent.classes.Magazine;
import com.example.servingwebcontent.classes.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends CrudRepository<Page, Long> {
    List<Page> findAllByArticle(Article article);
    List<Page> findAllByMagazine(Magazine magazine);
}
