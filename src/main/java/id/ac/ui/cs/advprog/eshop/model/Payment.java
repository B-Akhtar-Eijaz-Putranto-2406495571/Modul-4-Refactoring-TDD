package id.ac.ui.cs.advprog.eshop.model;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;
import lombok.Builder;

import java.util.Map;

@Getter @Builder
public class Payment {
    String id;
    String method;
    String status;
    Map<String, String> paymentData;

    public Payment(String id, String method, Map<String, String> paymentData) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null!");
        }
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException("Method cannot be null!");
        }
        if (paymentData == null) {
            throw new IllegalArgumentException("PaymentData cannot be null!");
        }

        this.id = id;
        if (PaymentMethod.contains(method)) {
            this.method = method;
        } else {
            throw new IllegalArgumentException();
        }
        this.paymentData = paymentData;
    }

    public Payment(String id, String method, String status, Map<String, String> paymentData) {
        this.id = id;
        if (PaymentMethod.contains(method)) {
            this.method = method;
        } else {
            throw new IllegalArgumentException();
        }
        this.paymentData = paymentData;
        this.setStatus(status);
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
