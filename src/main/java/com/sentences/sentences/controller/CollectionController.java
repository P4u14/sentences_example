package com.sentences.sentences.controller;

import com.sentences.sentences.csv.CSVHelper;
import com.sentences.sentences.csv.CSVService;
import com.sentences.sentences.entities.Collection;
import com.sentences.sentences.repositories.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(path = "/collections")
public class CollectionController {

    @Autowired
    private CollectionRepository collectionRepository;

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
    public @ResponseBody String uploadCollection(@RequestParam("file") MultipartFile file) {
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                csvService.save(file);
                return "Uploaded the file successfully: " + file.getOriginalFilename();
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
    public @ResponseBody String deleteAllCollections() {
        collectionRepository.deleteAll();
        return "Deleted All Collections";
    }

    @DeleteMapping(path = "/deleteById")
    public @ResponseBody String deleteSentenceById(@RequestParam Integer id) {
        collectionRepository.deleteById(id);
        return "Deleted Collection with id " + id;
    }
}
