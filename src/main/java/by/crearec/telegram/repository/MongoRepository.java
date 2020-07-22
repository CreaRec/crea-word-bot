package by.crearec.telegram.repository;

import by.crearec.telegram.configuration.Configurator;
import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;

import java.util.Collections;
import java.util.List;

public abstract class MongoRepository<T> {
	protected abstract Class<T> getRepositoryType();

	protected abstract String getCollectionName();

	public T getById(String id) {
		return getById(getCollectionName(), id);
	}

	public T getById(String collectionName, String id) {
		try (MongoClient mongoClient = Configurator.getInstance().getMongoClient()) {
			return mongoClient.getDatabase("crea_word_bot").getCollection(collectionName, getRepositoryType())
			                  .aggregate(Collections.singletonList(Aggregates.match(Filters.eq("_id", new ObjectId(id))))).first();
		}
	}

	public T getRandom() {
		return getRandom(getCollectionName());
	}

	public T getRandom(String collectionName) {
		try (MongoClient mongoClient = Configurator.getInstance().getMongoClient()) {
			return mongoClient.getDatabase("crea_word_bot").getCollection(collectionName, getRepositoryType()).aggregate(Collections.singletonList(Aggregates.sample(1)))
			                  .first();
		}
	}

	public void insert(T item) {
		insert(getCollectionName(), item);
	}

	public void insert(String collectionName, T item) {
		try (MongoClient mongoClient = Configurator.getInstance().getMongoClient()) {
			mongoClient.getDatabase("crea_word_bot").getCollection(collectionName, getRepositoryType()).insertOne(item);
		}
	}

	public void insert(List<T> items) {
		insert(getCollectionName(), items);
	}

	public void insert(String collectionName, List<T> items) {
		try (MongoClient mongoClient = Configurator.getInstance().getMongoClient()) {
			mongoClient.getDatabase("crea_word_bot").getCollection(collectionName, getRepositoryType()).insertMany(items);
		}
	}

	public void delete(String id) {
		delete(getCollectionName(), id);
	}

	public void delete(String collectionName, String id) {
		try (MongoClient mongoClient = Configurator.getInstance().getMongoClient()) {
			mongoClient.getDatabase("crea_word_bot").getCollection(collectionName, getRepositoryType()).deleteOne(Filters.eq("_id", id));
		}
	}

	public void createCollection(String collectionName) {
		try (MongoClient mongoClient = Configurator.getInstance().getMongoClient()) {
			mongoClient.getDatabase("crea_word_bot").createCollection(collectionName);
		}
	}

	public void dropCollection(String collectionName) {
		try (MongoClient mongoClient = Configurator.getInstance().getMongoClient()) {
			mongoClient.getDatabase("crea_word_bot").getCollection(collectionName).drop();
		}
	}
}
