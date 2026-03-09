package id.ac.ui.cs.advprog.eshop.model;
import lombok.Getter;
import lombok.Builder;
import lombok.Setter;

import java.util.Map;

@Getter @Builder
public class Payment {
    String id;
    String method;
    @Setter
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
    }

    public Payment(String id, String method, Map<String, String> paymentData, String status) {
    }


}
