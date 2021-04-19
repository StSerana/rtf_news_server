package com.example.servingwebcontent;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MagazineDto {
    private String name;
    private LocalDate date;
    private List<ArticleDto> articles;
}
