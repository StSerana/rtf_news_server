package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.classes.Magazine;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface MagazineRepository extends CrudRepository<Magazine, Long>{
    Magazine findByDate(LocalDate date);
    Magazine findById(long id);
    List<Magazine> findAll();
    void deleteById(long id);

    @Override
    <S extends Magazine> S save(S entity);
}
