package com.example.servingwebcontent;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaketRepository extends CrudRepository<Maket, Long> {
    Maket findByName(String Name);
}
