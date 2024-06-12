package in.back.end.project.config;



import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.back.end.project.model.ProductTransaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;


import java.io.InputStream;
import java.net.URL;
import java.util.List;

@Configuration

public class DbInit implements CommandLineRunner {
	 @Autowired
	    private ProductTransactionRepository repository;

	    private static final String API_URL = "https://s3.amazonaws.com/roxiler.com/product_transaction.json";

	    @Override
	    public void run(String... args) throws Exception {
	        // Fetch data from API
	        ObjectMapper mapper = new ObjectMapper();
	        List<ProductTransaction> transactions;

	        try (InputStream inputStream = new URL(API_URL).openStream()) {
	            transactions = mapper.readValue(inputStream, new TypeReference<List<ProductTransaction>>() {});
	        }

	        // Initialize database
	        repository.deleteAll();
	        repository.saveAll(transactions);
	    }

}
