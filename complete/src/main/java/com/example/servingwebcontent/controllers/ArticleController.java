package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.dtos.ArticleDto;
import com.example.servingwebcontent.dtos.PageDto;
import com.example.servingwebcontent.services.ArticleService;
import com.example.servingwebcontent.dtos.MagazineDto;
import com.example.servingwebcontent.dtos.ResultMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@CrossOrigin
public class ArticleController {

    private ArticleService articleService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @RequestMapping(value = "/magazine", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<MagazineDto> getAllMagazines() {
        return articleService.getAllMagazines();
    }

    @RequestMapping(value = "/article/{magazineId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<ArticleDto> getAllArticles(@PathVariable int magazineId){
        List<ArticleDto> articles = articleService.getAllArticles(magazineId);
        return articles;
    }

    @CrossOrigin
    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<PageDto> getPages() {
        List<PageDto> articles = articleService.getPages();
        return articles;
    }

    @CrossOrigin
    @RequestMapping(value = "/page/{page_id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody PageDto getPage(@PathVariable int page_id){
        PageDto articles = articleService.getPage(page_id);
        return articles;
    }

    @CrossOrigin
    @RequestMapping(value = "/page/{page_id}", method = RequestMethod.PUT, produces = "application/json")
    public @ResponseBody PageDto updatePage(@PathVariable int page_id, @RequestBody List<String> text){
        PageDto articles = articleService.updatePage(page_id, text);
        return articles;
    }

    @RequestMapping(value = "/magazine/{magazineId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<MagazineDto> getMagazine(@PathVariable int magazineId){
        MagazineDto magazineDto = articleService.getMagazine(magazineId);
        return Arrays.asList(magazineDto);
    }

    @RequestMapping(value = "/magazine/{magazineId}", method = RequestMethod.DELETE, produces = "application/json")
    public @ResponseBody String deleteMagazine(@PathVariable int magazineId){
        boolean status = articleService.deleteMagazine(magazineId);
        return gson.toJson(status ? ResultMessage.getSuccess() : ResultMessage.getError());
    }

    @RequestMapping(value = "/article/{articleId}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<ArticleDto> getArticle(@PathVariable int articleId){
        ArticleDto articleDto = articleService.getArticle(articleId);
        return Arrays.asList(articleDto);
    }

    @RequestMapping(value = "/create_magazine", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createMagazine(@RequestBody MagazineDto magazineDto){
        articleService.createMagazine(magazineDto);
        return gson.toJson(ResultMessage.getSuccess());
    }

    @RequestMapping(value = "/create_article/{magazineId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createArticle(@RequestBody ArticleDto articleDto, @PathVariable int magazineId){
        articleService.createArticle(magazineId, articleDto);
        return gson.toJson(ResultMessage.getSuccess());
    }

    @RequestMapping(value = "/create_page/{articleId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createPage(@RequestBody PageDto pageDto, @PathVariable int articleId){
        articleService.createPage(articleId, pageDto);
        return gson.toJson(ResultMessage.getSuccess());
    }


}
