package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.enums.OrderStatus;
import id.ac.ui.cs.advprog.eshop.enums.PaymentMethod;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.OrderRepository;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private List<PaymentValidator> paymentValidators;

    @Override
    public Payment addPayment(Order order, String method, Map<String, String> paymentData) {
        paymentData.put("orderId", order.getId());

        String status = PaymentStatus.REJECTED.getValue();

        for (PaymentValidator validator : paymentValidators) {
            if (validator.supports(method)) {
                status = validator.validate(paymentData);
                break;
            }
        }

        Payment payment = new Payment(UUID.randomUUID().toString(), method, status, paymentData);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment setStatus(Payment payment, String status) {
        payment.setStatus(status);
        paymentRepository.save(payment);

        String orderId = payment.getPaymentData().get("orderId");
        if (orderId != null) {
            Order order = orderRepository.findById(orderId);
            if (order != null) {
                if (PaymentStatus.SUCCESS.getValue().equals(status)) {
                    order.setStatus(OrderStatus.SUCCESS.getValue());
                } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
                    order.setStatus(OrderStatus.FAILED.getValue());
                }
                orderRepository.save(order);
            }
        }

        return payment;
    }

    @Override
    public Payment getPayment(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.getAllPayments();
    }
}