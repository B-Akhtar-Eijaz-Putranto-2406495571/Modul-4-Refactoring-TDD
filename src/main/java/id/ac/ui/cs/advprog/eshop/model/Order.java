package id.ac.ui.cs.advprog.eshop.model;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

import java.util.List;

@Getter @Builder
public class Order {
    String id;
    List<Product> products;
    Long orderTime;
    String author;
    @Setter
    String status;

    public Order(String id, List<Product> products, Long orderTime, String author) {

    }

    public Order(String id, List<Product> products, Long orderTime, String author, String status) {

    }
}

