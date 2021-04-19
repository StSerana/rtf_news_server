package com.example.servingwebcontent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
public interface MagazineRepository extends CrudRepository<Magazine, Long>{
    Magazine findByDate(LocalDate date);

    @Override
    <S extends Magazine> S save(S entity);
}
