package com.example.servingwebcontent;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final PageRepository pageRepository;
    private final MagazineRepository magazineRepository;
    private final MaketRepository maketRepository;

    public ArticleService(ArticleRepository articleRepository, PageRepository pageRepository, MagazineRepository magazineRepository, MaketRepository maketRepository) {
        this.articleRepository = articleRepository;
        this.pageRepository = pageRepository;
        this.magazineRepository = magazineRepository;
        this.maketRepository = maketRepository;
    }


    @Transactional
    public List<ArticleDto> getAllArticles(LocalDate magazineDate){
        List<Article> articles = articleRepository.findAllByMagazine(magazineRepository.findByDate(magazineDate));
        List<ArticleDto> articleDtos = new ArrayList<>();
        articles.forEach(article -> {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setName(article.getName());
            articleDto.setPages(getPagesForArticle(article));
            articleDtos.add(articleDto);
        });
        return articleDtos;
    }

    @Transactional
    public void createMagazine(MagazineDto magazineDto){
        Magazine magazine = new Magazine();
        magazine.setName(magazineDto.getName());
        magazine.setDate(magazineDto.getDate());
        magazineRepository.save(magazine);
        if (!magazineDto.getArticles().isEmpty()){
            magazineDto.getArticles()
                    .forEach(articleDto -> createArticle(magazine, articleDto));
        }
    }

    @Transactional
    public void createArticle(Magazine magazine, ArticleDto articleDto){
        Article article = new Article();
        article.setMagazine(magazine);
        article.setName(articleDto.getName());
        articleRepository.save(article);
        if (!articleDto.getPages().isEmpty()){
            articleDto.getPages()
                    .forEach(pageDto -> createPage(article, pageDto));
        }
    }

    @Transactional
    public void createPage(Article article, PageDto pageDto){
        Page page = new Page();
        page.setArticle(article);
        page.setNumber(pageDto.getNumber());
        page.setMaket(maketRepository.findByName(pageDto.getMaket()));
        page.setText(pageDto.getText());
        pageRepository.save(page);
    }

    @Transactional
    public List<PageDto> getPagesForArticle(Article article){
        List<PageDto> pageDtos = new ArrayList<>();
        pageRepository.findAllByArticle(article).forEach(
               page -> {
                   PageDto pageDto = new PageDto();
                   pageDto.setNumber(page.getNumber());
                   pageDto.setText(page.getText());
                   pageDto.setMaket(page.getMaket().getName());
                   pageDtos.add(pageDto);
               }
        );
        return pageDtos;
    }
}
