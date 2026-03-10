package id.ac.ui.cs.advprog.eshop.service;

import java.util.Map;

public interface PaymentValidator {
    boolean supports(String method);
    String validate(Map<String, String> paymentData);
}