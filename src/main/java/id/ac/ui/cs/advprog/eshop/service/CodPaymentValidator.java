package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CodPaymentValidator implements PaymentValidator {

    @Override
    public boolean supports(String method) {
        return PaymentMethod.COD.getValue().equals(method);
    }

    @Override
    public String validate(Map<String, String> paymentData) {
        String address = paymentData.get("address");
        String deliveryFee = paymentData.get("deliveryFee");
        if (address != null && !address.isEmpty() && deliveryFee != null && !deliveryFee.isEmpty()) {
            return PaymentStatus.SUCCESS.getValue();
        }
        return PaymentStatus.REJECTED.getValue();
    }
}