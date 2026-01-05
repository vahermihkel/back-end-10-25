package ee.mihkel.veebipood.model.supplier;

import lombok.Data;

@Data
public class Supplier1 {
    private int id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;
    private Supplier1Rating rating;
}
