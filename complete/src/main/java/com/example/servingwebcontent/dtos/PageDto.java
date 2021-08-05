package com.example.servingwebcontent.dtos;

import lombok.Data;

import java.util.List;

@Data

public class PageDto {
    private int id;
    private int number;
    private List<String> text;
    private String maket;
    private List<String> images;
}
