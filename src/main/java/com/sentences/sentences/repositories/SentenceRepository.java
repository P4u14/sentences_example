package com.sentences.sentences.repositories;

import com.sentences.sentences.entities.Sentence;
import org.springframework.data.repository.CrudRepository;

public interface SentenceRepository extends CrudRepository<Sentence, Integer> {
    Iterable<Sentence> findByCollectionId(Integer collectionId);
}
