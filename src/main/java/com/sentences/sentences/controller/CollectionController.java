package com.sentences.sentences.controller;

import com.sentences.sentences.csv.CSVHelper;
import com.sentences.sentences.csv.CSVService;
import com.sentences.sentences.entities.Collection;
import com.sentences.sentences.repositories.CollectionRepository;
import com.sentences.sentences.repositories.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(path = "/collections")
public class CollectionController {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private CSVService csvService;

    @GetMapping(path = "/")
    public @ResponseBody Iterable<Collection> getAllCollections() {
        return collectionRepository.findAll();
    }

    @GetMapping(path = "/getById")
    public @ResponseBody Optional<Collection> getCollection(@RequestParam Integer id) {
        return collectionRepository.findById(id);
    }

    @GetMapping(path = "/getByName")
    public @ResponseBody Collection getCollection(@RequestParam String name) {
        return collectionRepository.findByName(name);
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewCollection(@RequestParam String name) {
        Collection n = new Collection();
        n.setName(name);
        collectionRepository.save(n);
        return "Saved collection " + name;
    }

    @PostMapping(path = "/upload")
    public @ResponseBody String uploadCollection(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String name) {
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                if (name != null) {
                    csvService.save(file, name);
                    return "Uploaded the file successfully: " + name;
                } else {
                    csvService.save(file);
                    return "Uploaded the file successfully: " + file.getOriginalFilename();
                }
            } catch (Exception e) {
                return "Could not upload the file: " + file.getOriginalFilename() + "!" + e.getMessage();
            }
        }

        return "Please upload a csv file!";
    }

    @PutMapping(path = "/update")
    public @ResponseBody String updateCollection(@RequestParam Integer id, @RequestParam String name) {
        Optional<Collection> optN = collectionRepository.findById(id);
        if (optN.isEmpty()) {
            return "Collection with id " + id + "not found";
        }
        Collection n = optN.get();
        n.setName(name);
        collectionRepository.save(n);
        return "Updated collection with id " + id;
    }

    @DeleteMapping(path = "/deleteAll")
    @Transactional
    public @ResponseBody String deleteAllCollections() {
        sentenceRepository.deleteAll();
        collectionRepository.deleteAll();
        return "Deleted All Collections";
    }

    @DeleteMapping(path = "/deleteById")
    @Transactional
    public @ResponseBody String deleteSentenceById(@RequestParam Integer id) {
        try {
            sentenceRepository.deleteByCollectionId(id);
            collectionRepository.deleteById(id);
            return "Deleted Collection with id " + id;
        } catch (Exception e) {
            return "Collection with id " + id + " not found" + e.getMessage();
        }
    }
}
