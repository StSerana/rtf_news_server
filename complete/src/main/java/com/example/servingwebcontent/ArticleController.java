package com.example.servingwebcontent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }


    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody String getAllArticles(@RequestParam(name="magazine", required=false, defaultValue="0") String magazineDate){
        List<ArticleDto> articles = articleService.getAllArticles(LocalDate.parse(magazineDate));
        return gson.toJson(articles);
    }

    @RequestMapping(value = "/create_magazine", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody String createMagazine(@RequestBody MagazineDto magazineDto){
        articleService.createMagazine(magazineDto);
        return gson.toJson(ResultMessage.getSuccess());
    }
}
