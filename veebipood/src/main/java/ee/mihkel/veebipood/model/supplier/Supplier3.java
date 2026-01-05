package ee.mihkel.veebipood.model.supplier;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Supplier3 {
    private String error;
    private String total;
    private String page;
    private ArrayList<Supplier3Book> books;
}
