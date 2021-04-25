package com.example.servingwebcontent.dtos;

import com.example.servingwebcontent.dtos.ArticleDto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MagazineDto {
    private int id;
    private String name;
    private LocalDate date;
    private List<ArticleDto> articles;
}
