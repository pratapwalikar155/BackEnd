package in.back.end.project.repository;

import org.springframework.data.mongodb.repository.MongoRepository;


import java.util.Date;
import java.util.List;

public interface ProductTransactionRepository extends MongoRepository<ProductTransaction, String> {
	
	List<ProductTransaction> findByDateOfSaleBetween(Date start, Date end);
    List<ProductTransaction> findByTitleContainingOrDescriptionContainingOrPrice(String title, String description, double price);


}
