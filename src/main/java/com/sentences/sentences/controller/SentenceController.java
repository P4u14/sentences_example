package com.sentences.sentences.controller;

import com.sentences.sentences.entities.Sentence;
import com.sentences.sentences.repositories.CollectionRepository;
import com.sentences.sentences.repositories.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/sentences")
public class SentenceController {

    @Autowired
    private SentenceRepository sentenceRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @GetMapping("/")
    public @ResponseBody ResponseEntity<Object> getAllSentences() {
        try {
            Iterable<Sentence> sentences = sentenceRepository.findAll();
            return ResponseEntity.ok(sentences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/getById")
    public @ResponseBody ResponseEntity<Object> getSentence(@RequestParam Integer id) {
        try {
            Optional<Sentence> sentence = sentenceRepository.findById(id);
            if (sentence.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sentence with id " + id + " not found");
            }
            return ResponseEntity.ok(sentence.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @GetMapping(path = "/getByCollectionId")
    public @ResponseBody ResponseEntity<Object> getSentenceByCollectionId(@RequestParam Integer collectionId) {
        try {
            if (collectionRepository.findById(collectionId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No collection with id " + collectionId);
            }
            Iterable<Sentence> sentences = sentenceRepository.findByCollectionId(collectionId);
            return ResponseEntity.ok(sentences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(path = "/add")
    public @ResponseBody ResponseEntity<Object> addNewSentence(@RequestParam String text, @RequestParam String pronunciation, @RequestParam Integer collectionId) {
        try {
            if (collectionRepository.findById(collectionId).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No collection with id " + collectionId);
            }

            Sentence sentence = new Sentence();
            sentence.setText(text);
            sentence.setPronunciation(pronunciation);
            sentence.setCollectionId(collectionId);
            Sentence savedSentence = sentenceRepository.save(sentence);
            return ResponseEntity.ok("Saved sentence with id " + savedSentence.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping(path = "/update")
    public @ResponseBody ResponseEntity<Object> updateSentence(@RequestParam Integer id, @RequestParam(required = false) String text, @RequestParam(required = false) String pronunciation, @RequestParam(required = false) Integer collectionId) {
        try {
            Optional<Sentence> sentenceOpt = sentenceRepository.findById(id);
            if (sentenceOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sentence with id " + id + " not found");
            }
            Sentence sentence = sentenceOpt.get();
            if (text != null) {
                sentence.setText(text);
            }
            if (pronunciation != null) {
                sentence.setPronunciation(pronunciation);
            }
            if (collectionId != null) {
                if (collectionRepository.findById(collectionId).isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No collection with id " + collectionId);
                }
                sentence.setCollectionId(collectionId);
            }

            sentenceRepository.save(sentence);
            return ResponseEntity.ok("Updated sentence with id " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping(path = "/deleteAll")
    public @ResponseBody ResponseEntity<Object> deleteAllSentences() {
        try {
            sentenceRepository.deleteAll();
            return ResponseEntity.ok("Deleted all sentences");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping(path = "/deleteById")
    public @ResponseBody ResponseEntity<Object> deleteSentenceById(@RequestParam Integer id) {
        try {
            if (sentenceRepository.findById(id).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sentence with id " + id + " not found");
            }
            sentenceRepository.deleteById(id);
            return ResponseEntity.ok("Deleted sentence with id " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
