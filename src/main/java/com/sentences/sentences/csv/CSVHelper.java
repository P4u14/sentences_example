package com.sentences.sentences.csv;

import com.sentences.sentences.entities.Collection;
import com.sentences.sentences.entities.Sentence;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CSVHelper {

    public static String TYPE = "text/csv";
    static String[] HEADERS = { "id", "text", "pronunciation" };

    public static boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static Collection csvToCollection(MultipartFile file) {
        Collection collection = new Collection();
        collection.setName(file.getOriginalFilename());
        return collection;
    }

    public static List<Sentence> csvToSentences(InputStream is, Integer collectionId) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                        CSVFormat.DEFAULT.withAllowMissingColumnNames().withDelimiter('|'))) {

            List<Sentence> sentences = new ArrayList<Sentence>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                Sentence sentence = new Sentence();
                sentence.setText(csvRecord.get(1));
                sentence.setPronunciation(csvRecord.get(2));
                sentence.setCollectionId(collectionId);

                sentences.add(sentence);
            }

            return sentences;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

}
