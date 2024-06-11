package com.sentences.sentences.controller;

import com.sentences.sentences.entities.Sentence;
import com.sentences.sentences.repositories.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/sentences")
public class SentenceController {

    @Autowired
    private SentenceRepository sentenceRepository;

    @GetMapping("/")
    public @ResponseBody  Iterable<Sentence> getAllSentences() {
        return sentenceRepository.findAll();
    }

    @GetMapping(path = "/getById")
    public @ResponseBody
    Optional<Sentence> getSentence(@RequestParam Integer id) {
        return sentenceRepository.findById(id);
    }

    @GetMapping(path = "/getByCollectionId")
    public @ResponseBody Iterable<Sentence> getSentenceByCollectionId(@RequestParam Integer collectionId) {
        return sentenceRepository.findByCollectionId(collectionId);
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewSentence(@RequestParam String text, @RequestParam String pronunciation, @RequestParam Integer collectionId) {
        Sentence sentence = new Sentence();
        sentence.setText(text);
        sentence.setPronunciation(pronunciation);
        sentence.setCollectionId(collectionId);
        sentenceRepository.save(sentence);
        return "Saved";
    }

    @PutMapping(path = "/update")
    public @ResponseBody String updateSentence(@RequestParam Integer id, @RequestParam String text, @RequestParam String pronunciation, @RequestParam Integer collectionId) {
        Optional<Sentence> sentenceOpt = sentenceRepository.findById(id);
        if (sentenceOpt.isEmpty()) {
            return "Sentence with id " + id + " not found";
        }
        Sentence sentence = sentenceOpt.get();
        sentence.setText(text);
        sentence.setPronunciation(pronunciation);
        sentence.setCollectionId(collectionId);
        sentenceRepository.save(sentence);
        return "Updated";
    }

    @DeleteMapping(path = "/deleteAll")
    public @ResponseBody String deleteAllSentences() {
        sentenceRepository.deleteAll();
        return "Deleted all sentences";
    }

    @DeleteMapping(path = "/deleteById")
    public @ResponseBody String deleteSentenceById(@RequestParam Integer id) {
        sentenceRepository.deleteById(id);
        return "Deleted sentence with id " + id;
    }

}
