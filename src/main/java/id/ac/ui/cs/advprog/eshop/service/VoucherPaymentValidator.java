package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class VoucherPaymentValidator implements PaymentValidator {

    @Override
    public boolean supports(String method) {
        return PaymentMethod.VOUCHER.getValue().equals(method);
    }

    @Override
    public String validate(Map<String, String> paymentData) {
        String voucherCode = paymentData.get("voucherCode");
        if (voucherCode != null && voucherCode.length() == 16 && voucherCode.startsWith("ESHOP")) {
            long numericCount = voucherCode.chars().filter(Character::isDigit).count();
            if (numericCount == 8) {
                return PaymentStatus.SUCCESS.getValue();
            }
        }
        return PaymentStatus.REJECTED.getValue();
    }
}