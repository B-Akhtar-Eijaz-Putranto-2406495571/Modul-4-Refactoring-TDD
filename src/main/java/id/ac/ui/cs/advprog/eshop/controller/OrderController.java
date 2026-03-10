package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/create")
    public String createOrderPage(Model model) {
        return "createOrder";
    }

    @PostMapping("/create")
    public String createOrderPost(@RequestParam("author") String author) {
        List<Product> allProducts = productService.findAll();
        List<Product> orderProducts = new ArrayList<>();
        if (!allProducts.isEmpty()) {
            orderProducts.add(allProducts.get(0));
        } else {
            Product product = new Product();
            product.setProductId(UUID.fromString("158e8ef4-7e27-4945-acca-09e570459c2b"));
            product.setProductName("Sampo Cap Bambang");
            product.setProductQuantity(1);
            orderProducts.add(product);
        }

        Order newOrder = new Order(java.util.UUID.randomUUID().toString(), orderProducts, System.currentTimeMillis(), author);

        orderService.createOrder(newOrder);

        return "redirect:/order/history";
    }

    @GetMapping("/history")
    public String historyOrderPage() {
        return "orderHistoryForm";
    }

    @PostMapping("/history")
    public String historyOrderPost(@RequestParam("author") String author, Model model) {
        List<Order> orders = orderService.findAllByAuthor(author);

        model.addAttribute("orders", orders);
        model.addAttribute("author", author);
        return "orderHistoryList";
    }

    @GetMapping("/pay/{orderId}")
    public String payOrderPage(@PathVariable("orderId") String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "payOrder";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrderPost(@PathVariable("orderId") String orderId, @RequestParam("method") String method, @RequestParam Map<String, String> allParams) {

        Order order = orderService.findById(orderId);

        Map<String, String> paymentData = new HashMap<>();
        if ("VOUCHER".equals(method)) {
            paymentData.put("voucherCode", allParams.get("voucherCode"));
        } else if ("COD".equals(method)) {
            paymentData.put("address", allParams.get("address"));
            paymentData.put("deliveryFee", allParams.get("deliveryFee"));
        }

        Payment payment = paymentService.addPayment(order, method, paymentData);
        return "redirect:/payment/detail/" + payment.getId();
    }
}