package com.example.servingwebcontent.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ArticleDto {
    private int id;
    private String name;
    private List<PageDto> pages;
    private String author;
}
