package com.sentences.sentences.controller;

import com.sentences.sentences.csv.CSVHelper;
import com.sentences.sentences.csv.CSVService;
import com.sentences.sentences.entities.Collection;
import com.sentences.sentences.repositories.CollectionRepository;
import com.sentences.sentences.repositories.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public @ResponseBody ResponseEntity<Object> getAllCollections() {
        try {
            Iterable<Collection> collections = collectionRepository.findAll();
            return ResponseEntity.ok(collections);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/getById")
    public @ResponseBody ResponseEntity<Object> getCollection(@RequestParam Integer id) {
        try {
            Optional<Collection> collectionOpt = collectionRepository.findById(id);
            if (collectionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection with id " + id + " not found");
            }
            return ResponseEntity.ok(collectionOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/getByName")
    public @ResponseBody ResponseEntity<Object> getCollection(@RequestParam String name) {
        try {
            Optional<Collection> collectionOpt = collectionRepository.findByName(name);
            if (collectionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection with name " + name + " not found");
            }
            return ResponseEntity.ok(collectionOpt.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(path = "/add")
    public @ResponseBody ResponseEntity<Object> addNewCollection(@RequestParam String name) {
        try {
            Collection collection = new Collection();
            collection.setName(name);
            Collection savedCollection = collectionRepository.save(collection);
            return ResponseEntity.ok("Saved collection with id " + savedCollection.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(path = "/upload")
    public @ResponseBody ResponseEntity<Object> uploadCollection(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String name) {
        try {
            if (CSVHelper.hasCSVFormat(file)) {
                csvService.save(file, name);
                return ResponseEntity.ok("Uploaded the file successfully: " + file.getOriginalFilename());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a csv file!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping(path = "/update")
    public @ResponseBody ResponseEntity<Object> updateCollection(@RequestParam Integer id, @RequestParam String name) {
        try {
            Optional<Collection> collectionOpt = collectionRepository.findById(id);
            if (collectionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection with id " + id + " not found");
            }
            Collection collection = collectionOpt.get();
            collection.setName(name);
            collectionRepository.save(collection);
            return ResponseEntity.ok("Updated collection with id " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping(path = "/deleteAll")
    @Transactional
    public @ResponseBody ResponseEntity<Object> deleteAllCollections() {
        try {
            sentenceRepository.deleteAll();
            collectionRepository.deleteAll();
            return ResponseEntity.ok("Deleted all sentences");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping(path = "/deleteById")
    @Transactional
    public @ResponseBody ResponseEntity<Object> deleteSentenceById(@RequestParam Integer id) {
        try {
            Optional<Collection> collectionOpt = collectionRepository.findById(id);
            if (collectionOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Collection with id " + id + " not found");
            }
            sentenceRepository.deleteByCollectionId(id);
            collectionRepository.deleteById(id);
            return ResponseEntity.ok("Deleted collection with id " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
