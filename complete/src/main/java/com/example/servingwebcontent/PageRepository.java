package com.example.servingwebcontent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends CrudRepository<Page, Long> {
    List<Page> findAllByArticle(Article article);
}
