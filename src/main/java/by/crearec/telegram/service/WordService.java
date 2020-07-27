package by.crearec.telegram.service;

import by.crearec.telegram.entity.mongo.Word;
import by.crearec.telegram.repository.WordRepository;

import java.util.List;

public class WordService {

	private final WordRepository wordRepository;

	public WordService(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	public Word getRandom(String collectionName) {
		return wordRepository.getRandom(collectionName);
	}

	public void save(String collectionName, List<Word> words) {
		wordRepository.insert(collectionName, words);
	}

	public void deleteAllByUserId(String userId) {
		wordRepository.dropCollection(userId);
	}
}
