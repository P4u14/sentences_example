package com.sentences.sentences.csv;

import com.sentences.sentences.entities.Collection;
import com.sentences.sentences.entities.Sentence;
import com.sentences.sentences.repositories.CollectionRepository;
import com.sentences.sentences.repositories.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class CSVService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SentenceRepository sentenceRepository;

    public void save(MultipartFile file) {
        try {
            Collection collection = collectionRepository.save(CSVHelper.csvToCollection(file));
            List<Sentence> sentences = CSVHelper.csvToSentences(file.getInputStream(), collection.getId());
            sentenceRepository.saveAll(sentences);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }
}
