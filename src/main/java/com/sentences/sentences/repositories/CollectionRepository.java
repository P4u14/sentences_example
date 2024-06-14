package com.sentences.sentences.repositories;

import com.sentences.sentences.entities.Collection;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CollectionRepository extends CrudRepository<Collection, Integer> {
    Optional<Collection> findByName(String name);
}
