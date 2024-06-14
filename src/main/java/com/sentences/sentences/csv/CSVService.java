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

    public void save(MultipartFile file, String name) {
        if (name == null || name.isEmpty()) {
            name = file.getOriginalFilename();
            if (name != null && name.toLowerCase().endsWith(".csv")) {
                name = name.substring(0, name.length() - 4);
            }
        }

        Collection newCollection = new Collection();
        newCollection.setName(name);

        try {
            Collection savedCollection = collectionRepository.save(newCollection);
            List<Sentence> sentences = CSVHelper.csvToSentences(file.getInputStream(), savedCollection.getId());
            sentenceRepository.saveAll(sentences);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

}
