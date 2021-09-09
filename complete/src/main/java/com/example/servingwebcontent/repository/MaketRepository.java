package com.example.servingwebcontent.repository;

import com.example.servingwebcontent.classes.Maket;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MaketRepository extends CrudRepository<Maket, Long> {
    Optional<Maket> findByName(String Name);
    Maket findById(long id);
}
