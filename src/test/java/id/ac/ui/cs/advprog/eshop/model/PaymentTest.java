package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        this.paymentData = new HashMap<>();
        this.paymentData.put("voucherCode", "ESHOP1234ABC5678");
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment("3f4f8b91-4606-46ce-8562-f363d823c8cb", "VOUCHER", this.paymentData);

        assertEquals("3f4f8b91-4606-46ce-8562-f363d823c8cb", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals(this.paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithStatus() {
        Payment payment = new Payment("3f4f8b91-4606-46ce-8562-f363d823c8cb", "VOUCHER", "SUCCESS", this.paymentData);

        assertEquals("3f4f8b91-4606-46ce-8562-f363d823c8cb", payment.getId());
        assertEquals("VOUCHER", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(this.paymentData, payment.getPaymentData());
    }

    @Test
    void testCreatePaymentWithInvalidStatus() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("3f4f8b91-4606-46ce-8562-f363d823c8cb", "VOUCHER", "MEOW", this.paymentData)
        );
    }

    @Test
    void testCreatePaymentWithNullId() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment(null, "VOUCHER", paymentData)
        );
    }

    @Test
    void testCreatePaymentWithNullMethod() {
        assertThrows(IllegalArgumentException.class, () ->
                new Payment("3f4f8b91-4606-46ce-8562-f363d823c8cb", null, paymentData)
        );
    }

    @Test
    void testSetStatusToRejected() {
        Payment payment = new Payment("3f4f8b91-4606-46ce-8562-f363d823c8cb", "VOUCHER", this.paymentData);
        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetStatusToInvalidStatus() {
        Payment payment = new Payment("3f4f8b91-4606-46ce-8562-f363d823c8cb", "VOUCHER", this.paymentData);
        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }
}