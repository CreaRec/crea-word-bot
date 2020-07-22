package by.crearec.telegram.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class Configurator {
	private final MongoClientSettings mongoClientSettings;
	@Getter
	private final Client client;

	private Configurator() {
		this.mongoClientSettings = createMongoClientSettings();
		this.client = createClient();
	}

	public static class ConfiguratorHolder {
		public static final Configurator HOLDER_INSTANCE = new Configurator();
	}

	public static Configurator getInstance() {
		return Configurator.ConfiguratorHolder.HOLDER_INSTANCE;
	}

	private MongoClientSettings createMongoClientSettings() {
		ConnectionString connectionString = new ConnectionString(
				"mongodb+srv://CreaWordBot:82138213@creawordbotcluster.3sax4.mongodb.net/crea_word_bot?retryWrites=true&w=majority");
		CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
		CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
		return MongoClientSettings.builder().applyConnectionString(connectionString).codecRegistry(codecRegistry).build();

	}

	private Client createClient() {
		ClientConfig configuration = new ClientConfig();
		configuration.property(ClientProperties.FOLLOW_REDIRECTS, Boolean.FALSE);
		configuration.register(ObjectMapperProvider.class);
		configuration.register(JacksonFeature .class);
		return ClientBuilder.newClient(configuration);
	}

	public MongoClient getMongoClient() {
		return MongoClients.create(mongoClientSettings);
	}
}
