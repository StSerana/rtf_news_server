package com.example.servingwebcontent;

import lombok.Data;
import org.springframework.data.domain.jaxb.SpringDataJaxb;

import java.util.List;

@Data
public class ArticleDto {
    private String name;
    private List<PageDto> pages;
}
