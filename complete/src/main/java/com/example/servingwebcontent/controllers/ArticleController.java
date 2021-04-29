package com.example.servingwebcontent.controllers;

import com.example.servingwebcontent.dtos.ArticleDto;
import com.example.servingwebcontent.dtos.PageDto;
import com.example.servingwebcontent.services.ArticleService;
import com.example.servingwebcontent.dtos.MagazineDto;
import com.example.servingwebcontent.dtos.ResultMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @CrossOrigin
    @RequestMapping(value = "/magazine", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<MagazineDto> getAllMagazines() {
        System.out.println("get all magazines");
        return articleService.getAllMagazines();
    }

    @CrossOrigin
    @RequestMapping(value = "/article", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<ArticleDto> getAllArticles(@RequestParam(name="magazineId", required=false, defaultValue="0") int magazineId){
        List<ArticleDto> articles = articleService.getAllArticles(magazineId);
        return articles;
    }

    @RequestMapping(value = "/magazine/{magazine_id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<MagazineDto> getMagazine(@PathVariable int magazine_id){
        MagazineDto magazineDto = articleService.getMagazine(magazine_id);
        return Arrays.asList(magazineDto);
    }

    @RequestMapping(value = "/article/{article_id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody List<ArticleDto> getArticle(@PathVariable int article_id){
        ArticleDto articleDto = articleService.getArticle(article_id);
        return Arrays.asList(articleDto);
    }

    @RequestMapping(value = "/create_magazine", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createMagazine(@RequestBody MagazineDto magazineDto){
        articleService.createMagazine(magazineDto);
        return gson.toJson(ResultMessage.getSuccess());
    }

    @RequestMapping(value = "/create_article/{magazine_id}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createArticle(@RequestBody ArticleDto articleDto, @PathVariable int magazine_id){
        articleService.createArticle(magazine_id, articleDto);
        return gson.toJson(ResultMessage.getSuccess());
    }

    @RequestMapping(value = "/create_page/{article_id}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createPage(@RequestBody PageDto pageDto, @PathVariable int article_id){
        articleService.createPage(article_id, pageDto);
        return gson.toJson(ResultMessage.getSuccess());
    }


}
