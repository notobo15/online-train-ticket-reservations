package com.trainticketbooking.app.Controllers.API;


import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.trainticketbooking.app.Services.impl.PayPalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    // Endpoint to initiate the payment
    @PostMapping("/pay")
    public String  createPayment(@RequestParam Double total,
                                 @RequestParam String currency,
                                 @RequestParam String description) {
        try {
            String cancelUrl = "http://localhost:8080/api/paypal/cancel";
            String successUrl = "http://localhost:8080/api/paypal/success";

            Payment payment = payPalService.createPayment(total, currency, "paypal",
                    "sale", description, cancelUrl,
                    successUrl);

            return payment.getLinks().stream()
                    .filter(link -> "approval_url".equals(link.getRel()))
                    .findFirst()
                    .map(link -> link.getHref())
                    .orElse("Error: PayPal approval URL not found.");


        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return null;
        }
    }
    // Endpoint to execute the payment after the user approves it
    @GetMapping("/success")
    public String successPayment(@RequestParam("paymentId") String paymentId,
                                 @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = payPalService.executePayment(paymentId, payerId);
            return "Payment executed successfully: " + payment.toJSON();
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return "Payment execution failed!";
        }
    }

    // Endpoint to cancel the payment
    @GetMapping("/cancel")
    public String cancelPayment() {
        return "Payment was cancelled.";
    }
}