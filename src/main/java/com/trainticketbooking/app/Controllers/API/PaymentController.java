package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.OrderRequestDto;
import com.trainticketbooking.app.Dtos.OrderResponseDto;
import com.trainticketbooking.app.Dtos.PaymentDto;
import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.Payment;
import com.trainticketbooking.app.Services.impl.BookingService;
import com.trainticketbooking.app.Services.impl.PaymentService;
import com.trainticketbooking.app.Services.impl.VNPayService;
import com.trainticketbooking.app.Utils.UrlUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private BookingService bookingService; // Service to handle booking logic

    @Autowired
    private PaymentService paymentService; // Service to handle payment logic

    @PostMapping("order/checkout")
    public ResponseEntity<OrderResponseDto> submitOrder(@RequestBody OrderRequestDto orderRequestDto, HttpServletRequest request) {

        String baseUrl = UrlUtils.getBaseUrl(request);
        String vnpayUrl = vnPayService.createOrder(orderRequestDto.getAmount(), orderRequestDto.getOrderInfo(), baseUrl);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setMessage("OK");
        responseDto.setStatus(true);
        responseDto.setUrl(vnpayUrl);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/order/payment/callback")
    public ResponseEntity<?> getPaymentResult(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");
        String responseCode = request.getParameter("vnp_ResponseCode");

        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setOrderId(orderInfo);
        paymentDto.setPaymentTime(paymentTime);
        paymentDto.setTransactionId(transactionId);
        paymentDto.setTotalPrice(totalPrice);
        paymentDto.setBankCode(bankCode);
        paymentDto.setResponseCode(responseCode);

        Booking booking = new Booking();
        booking.setTotalPrice(Double.parseDouble(totalPrice));
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTime(LocalDateTime.now());
        payment.setMethod("VNPay");
        payment.setStatus(paymentStatus == 1 ? "Paid" : "Failed");

        paymentService.save(payment);

        return ResponseEntity.ok(paymentDto);

    }
}
