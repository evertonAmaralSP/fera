package br.com.abril.fera.config.settings;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

@Configuration
public class MongoConfig {

	@Value("${dataSourceMongoDB.database}")
	private String database;
	@Value("${dataSourceMongoDB.url}")
	private String url;


	public @Bean
	MongoDbFactory mongoDbFactory() throws Exception {
		MongoClient mongo = new MongoClient(url);
		SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(mongo, database);
		return simpleMongoDbFactory;

	}
	
	public @Bean
	MongoOperations mongoOperation() throws Exception {
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
		// show error, should off on production to speed up performance
		mongoTemplate.setWriteConcern(WriteConcern.SAFE);
		return (MongoOperations) mongoTemplate ;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
}