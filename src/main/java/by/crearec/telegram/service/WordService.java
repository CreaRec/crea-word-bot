package by.crearec.telegram.service;

import by.crearec.telegram.entity.mongo.Word;
import by.crearec.telegram.repository.WordRepository;

public class WordService {

	private final WordRepository wordRepository;

	public WordService(WordRepository wordRepository) {
		this.wordRepository = wordRepository;
	}

	public Word getRandom(String collectionName) {
		return wordRepository.getRandom(collectionName);
	}
}
