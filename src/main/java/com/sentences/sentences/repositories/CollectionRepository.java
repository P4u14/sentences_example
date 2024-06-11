package com.sentences.sentences.repositories;

import com.sentences.sentences.entities.Collection;
import org.springframework.data.repository.CrudRepository;

public interface CollectionRepository extends CrudRepository<Collection, Integer> {
    Collection findByName(String name);
}
