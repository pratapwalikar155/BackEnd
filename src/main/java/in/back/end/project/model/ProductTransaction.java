package in.back.end.project.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "product_transactions")

public class ProductTransaction {
	@Id
    private String id;
    private int productId;
    private String title;
    private String description;
    private double price;
    private Date dateOfSale;
    private String category;
    private boolean sold;

}
