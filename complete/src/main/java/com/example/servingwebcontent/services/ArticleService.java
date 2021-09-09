package com.example.servingwebcontent.services;

import com.example.servingwebcontent.classes.Article;
import com.example.servingwebcontent.classes.Image;
import com.example.servingwebcontent.classes.Magazine;
import com.example.servingwebcontent.classes.Page;
import com.example.servingwebcontent.dtos.ArticleDto;
import com.example.servingwebcontent.dtos.MagazineDto;
import com.example.servingwebcontent.dtos.PageDto;
import com.example.servingwebcontent.repository.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final PageRepository pageRepository;
    private final MagazineRepository magazineRepository;
    private final MaketRepository maketRepository;
    private final ImageRepository imageRepository;

    @Value("${nullImageName}")
    private String nullImage;

    public ArticleService(ArticleRepository articleRepository, PageRepository pageRepository, MagazineRepository magazineRepository, MaketRepository maketRepository, ImageRepository imageRepository) {
        this.articleRepository = articleRepository;
        this.pageRepository = pageRepository;
        this.magazineRepository = magazineRepository;
        this.maketRepository = maketRepository;
        this.imageRepository = imageRepository;
    }

    @Transactional
    public List<MagazineDto> getAllMagazines(){
        List<MagazineDto> magazineDtos = new ArrayList<>();
        magazineRepository.findAll()
                .forEach(magazine -> {
                    MagazineDto magazineDto = new MagazineDto();
                    magazineDto.setId(magazine.getId().intValue());
                    magazineDto.setName(magazine.getName());
                    magazineDto.setDate(magazine.getDate());
                    MainPages mainPages = getMainPagesForMagazine(magazine);
                    magazineDto.setMain(Arrays.asList(mainPages.getMain()));
                    magazineDto.setChapterList(Arrays.asList(mainPages.getChapters()));
                    magazineDto.setArticles(getAllArticles(magazine.getId().intValue()));
                    magazineDtos.add(magazineDto);
                });
        return magazineDtos;
    }


    @Transactional
    public List<ArticleDto> getAllArticles(int magazineId){
        List<Article> articles = articleRepository.findAllByMagazine(magazineRepository.findById(magazineId));
        articles.sort((a1, a2)-> (int) (a1.getId() - a2.getId()));
        List<ArticleDto> articleDtos = new ArrayList<>();
        articles.forEach(article -> {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setId(article.getId().intValue());
            articleDto.setAuthor(article.getAuthor());
            articleDto.setName(article.getName());
            articleDto.setPages(getPagesForArticle(article));
            articleDtos.add(articleDto);
        });
        articleDtos.sort(Comparator.comparingInt(ArticleDto::getId));
        return articleDtos;
    }

    @Transactional
    public MagazineDto getMagazine(int magazineId){
        Magazine magazine = magazineRepository.findById(magazineId);
        MagazineDto magazineDto = new MagazineDto();
        magazineDto.setId(magazineId);
        magazineDto.setDate(magazine.getDate());
        magazineDto.setName(magazine.getName());
        MainPages mainPages = getMainPagesForMagazine(magazine);
        magazineDto.setMain(Arrays.asList(mainPages.getMain()));
        magazineDto.setChapterList(Arrays.asList(mainPages.getChapters()));
        magazineDto.setArticles(getAllArticles(magazineId));
        return magazineDto;
    }

    @Transactional
    public void createMagazine(MagazineDto magazineDto){
        Magazine magazine = new Magazine();
        magazine.setName(magazineDto.getName());
        magazine.setDate(magazineDto.getDate());
        magazineRepository.save(magazine);
        createMainPages(magazine.getId().intValue(), magazineDto.getMain().get(0), magazineDto.getChapterList().get(0));
        if (!magazineDto.getArticles().isEmpty()){
            magazineDto.getArticles()
                    .forEach(articleDto -> createArticle(magazine.getId().intValue(), articleDto));
        }
    }

    @Transactional
    public void createArticle(int magazine_id, ArticleDto articleDto){
        Magazine magazine = magazineRepository.findById(magazine_id);
        Article article = new Article();
        article.setAuthor(articleDto.getAuthor());
        article.setMagazine(magazine);
        article.setName(articleDto.getName());
        articleRepository.save(article);
        if (!articleDto.getPages().isEmpty()){
            articleDto.getPages()
                    .forEach(pageDto -> createPage(article.getId().intValue(), pageDto));
        }
    }

    @Transactional
    public void createMainPages(int magazine_id, PageDto main, PageDto chapters){
        Magazine magazine = magazineRepository.findById(magazine_id);
        Page mainPage = new Page(),
                chaptersPage = new Page();

        mainPage.setNumber(0);
        mainPage.setMaket(maketRepository.findByName(main.getMaket()).orElse(maketRepository.findById(1)));
        mainPage.setMagazine(magazine);
        pageRepository.save(mainPage);

        chaptersPage.setNumber(1);
        chaptersPage.setMaket(maketRepository.findByName(chapters.getMaket()).orElse(maketRepository.findById(1)));
        chaptersPage.setMagazine(magazine);
        pageRepository.save(chaptersPage);
    }

    @Transactional
    public void createPage(int article_id, PageDto pageDto){
        Article article = articleRepository.findById(article_id);
        Page page = new Page();
        page.setArticle(article);
        page.setNumber(pageDto.getNumber());
        page.setMaket(maketRepository.findByName(pageDto.getMaket()).orElse(maketRepository.findById(1)));
        page.setText(pageDto.getText());
        pageRepository.save(page);
        if (!pageDto.getImages().isEmpty()){
            pageDto.getImages().forEach(image -> createImage(image, page));
        }
    }

    private void createImage(String imageName, Page page) {
        if (imageName != nullImage) {
            Image image = new Image();
            image.setName(imageName);
            image.setPage(page);
            imageRepository.save(image);
        }
    }

    public ArticleDto getArticle(int articleId){
        Article article = articleRepository.findById(articleId);
        ArticleDto articleDto = new ArticleDto();
        articleDto.setId(articleId);
        articleDto.setName(article.getName());
        articleDto.setPages(getPagesForArticle(article));
        return articleDto;
    }

    @Transactional
    public List<PageDto> getPagesForArticle(Article article){
        List<PageDto> pageDtos = new ArrayList<>();
        List<Page> pages = pageRepository.findAllByArticle(article);
        pages.sort((page1, page2)->{
            if (page1.getNumber() > page2.getNumber())
                return 1;
            else if (page1.getNumber() == page2.getNumber())
                return 0;
            else
                return -1;
        });
        pages.forEach(
               page -> {
                   pageDtos.add(createPageDto(page));
               }
        );
        return pageDtos;
    }

    public PageDto createPageDto(Page page){
        PageDto pageDto = new PageDto();
        pageDto.setId(page.getId().intValue());
        pageDto.setNumber(page.getNumber());
        pageDto.setText(page.getText());
        pageDto.setMaket(page.getMaket().getName());
        pageDto.setImages(imageRepository.findAllByPage(page)
                .stream()
                .map(Image::getName)
                .collect(Collectors.toList()));
        return pageDto;
    }

    @Transactional
    public MainPages getMainPagesForMagazine(Magazine magazine){
        List<Page> mainPages = pageRepository.findAllByMagazine(magazine);
        MainPages mainPagesDto = new MainPages();
        mainPages.stream().forEach(page -> {
            switch (page.getNumber()) {
            case 0:
                mainPagesDto.setMain(createPageDto(page));
                break;
            case 1:
                mainPagesDto.setChapters(createPageDto(page));
                break;
            default:
                break;
            }
        }
        );
        return mainPagesDto;
    }

    @Transactional
    public boolean deleteMagazine(int magazine_id) {
        try{
            Magazine magazine = magazineRepository.findById(magazine_id);
            List<Article> articles = articleRepository.findAllByMagazine(magazine);
            log.info("delete images by magazine");
            pageRepository.findAllByMagazine(magazine).forEach(imageRepository::deleteAllByPage);
            pageRepository.deleteAllByMagazine(magazine);
            articles.forEach(article -> {
                log.info("delete images by article");
                pageRepository.findAllByArticle(article).forEach(imageRepository::deleteAllByPage);
                pageRepository.deleteAllByArticle(article);
            });
            articleRepository.deleteAllByMagazine(magazine);
            magazineRepository.deleteById(magazine_id);
            return true;
        } catch (Exception ex){
            log.error("problems: " + ex.getMessage());
            return false;
        }
    }

    @Transactional
    public PageDto getPage(int pageId) {
        Page page = pageRepository.findById(pageId);
        return createPageDto(page);
    }

    @Transactional
    public List<PageDto> getPages() {
        List<Page> pages = (List<Page>) pageRepository.findAll();
        return pages.stream().map(page -> createPageDto(page)).collect(Collectors.toList());
    }

    @Transactional
    public PageDto updatePage(int page_id, List<String> text) {
        Page page = pageRepository.findById(page_id);
        page.setText(text);
        pageRepository.save(page);
        return createPageDto(page);
    }

    @Data
    public class MainPages {
        public PageDto main;
        public PageDto chapters;
    }
}
