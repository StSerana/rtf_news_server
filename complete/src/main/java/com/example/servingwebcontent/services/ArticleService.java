package com.example.servingwebcontent.services;

import com.example.servingwebcontent.classes.Article;
import com.example.servingwebcontent.classes.Magazine;
import com.example.servingwebcontent.classes.Page;
import com.example.servingwebcontent.dtos.ArticleDto;
import com.example.servingwebcontent.dtos.MagazineDto;
import com.example.servingwebcontent.dtos.PageDto;
import com.example.servingwebcontent.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final PageRepository pageRepository;
    private final MagazineRepository magazineRepository;
    private final MaketRepository maketRepository;
    private final ImageRepository imageRepository;

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
                    magazineDto.setMain(mainPages.main);
                    magazineDto.setChapterList(mainPages.chapters);
                    magazineDto.setArticles(getAllArticles(magazine.getId().intValue()));
                    magazineDtos.add(magazineDto);
                });
        return magazineDtos;
    }


    @Transactional
    public List<ArticleDto> getAllArticles(int magazineId){
        List<Article> articles = articleRepository.findAllByMagazine(magazineRepository.findById(magazineId));
        List<ArticleDto> articleDtos = new ArrayList<>();
        articles.forEach(article -> {
            ArticleDto articleDto = new ArticleDto();
            articleDto.setId(article.getId().intValue());
            articleDto.setAuthor(article.getAuthor());
            articleDto.setName(article.getName());
            articleDto.setPages(getPagesForArticle(article));
            articleDtos.add(articleDto);
        });
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
        magazineDto.setMain(mainPages.main);
        magazineDto.setChapterList(mainPages.chapters);
        magazineDto.setArticles(getAllArticles(magazineId));
        return magazineDto;
    }

    @Transactional
    public void createMagazine(MagazineDto magazineDto){
        Magazine magazine = new Magazine();
        magazine.setName(magazineDto.getName());
        magazine.setDate(magazineDto.getDate());
        magazineRepository.save(magazine);
        createMainPages(magazine.getId().intValue(), magazineDto.getMain(), magazineDto.getChapterList());
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
        mainPage.setMaket(maketRepository.findByName(main.getMaket()));
        mainPage.setMagazine(magazine);
        pageRepository.save(mainPage);

        chaptersPage.setNumber(1);
        chaptersPage.setMaket(maketRepository.findByName(chapters.getMaket()));
        chaptersPage.setMagazine(magazine);
        pageRepository.save(chaptersPage);
    }

    @Transactional
    public void createPage(int article_id, PageDto pageDto){
        Article article = articleRepository.findById(article_id);
        Page page = new Page();
        page.setArticle(article);
        page.setNumber(pageDto.getNumber());
        page.setMaket(maketRepository.findByName(pageDto.getMaket()));
        page.setText(pageDto.getText());
        pageRepository.save(page);
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
        pageRepository.findAllByArticle(article).forEach(
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
                .map(image -> image.getName())
                .collect(Collectors.toList()));
        return pageDto;
    }

    @Transactional
    public MainPages getMainPagesForMagazine(Magazine magazine){
        List<Page> mainPages = pageRepository.findAllByMagazine(magazine);
        AtomicReference<PageDto> main = new AtomicReference<PageDto>();
        AtomicReference<PageDto> chapter = new AtomicReference<>(new PageDto());
        mainPages.stream().forEach(page -> {
            switch (page.getNumber()) {
            case 0:
                main.set(createPageDto(page));
                break;
            case 1:
                chapter.set(createPageDto(page));
                break;
            default:
                break;
            }
        }
        );
        return new MainPages(main.get(), chapter.get());
    }

    public class MainPages {
        public final PageDto main;
        public final PageDto chapters;

        public MainPages(PageDto main, PageDto chapters) {
            this.main = main;
            this.chapters = chapters;
        }
    }
}
