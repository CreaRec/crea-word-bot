package by.crearec.telegram.repository;

import by.crearec.telegram.entity.mongo.Word;

public class WordRepository extends MongoRepository<Word> {

	@Override
	protected Class<Word> getRepositoryType() {
		return Word.class;
	}

	@Override
	protected String getCollectionName() {
		return "dictionary";
	}
}
