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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class PaymentApiController {

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

//    @GetMapping("/order/payment/callback")
//    public ResponseEntity<?> getPaymentResult(HttpServletRequest request) {
//        // Lấy thông tin trả về từ VNPay
//        int paymentStatus = vnPayService.orderReturn(request);
//
//        // Lấy các tham số từ request
//        String orderInfo = request.getParameter("vnp_OrderInfo"); // orderInfo chính là ID của booking
//        String paymentTime = request.getParameter("vnp_PayDate");
//        String transactionId = request.getParameter("vnp_TransactionNo");
//        String totalPrice = request.getParameter("vnp_Amount");
//        String bankCode = request.getParameter("vnp_BankCode");
//        String responseCode = request.getParameter("vnp_ResponseCode");
//
//        // Tìm Booking từ orderInfo
//        Optional<Booking> optionalBooking = bookingService.getById(Integer.valueOf(orderInfo));
//        if (!optionalBooking.isPresent()) {
//            // Nếu không tìm thấy booking với orderInfo thì trả về lỗi
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Booking not found with order info: " + orderInfo);
//        }
//
//        // Lấy booking từ Optional
//        Booking booking = optionalBooking.get();
//
//        // Cập nhật lại thông tin booking (nếu cần)
//        booking.setTotalPrice(Double.parseDouble(totalPrice)); // Cập nhật tổng giá trị
//        booking.setStatus("Paid");
//        // Lưu thông tin payment
//        Payment payment = new Payment();
//        payment.setBooking(booking);
//        payment.setTime(LocalDateTime.now());
//        payment.setMethod("VNPay");
//        payment.setStatus(paymentStatus == 1 ? "Paid" : "Failed");
//        payment.setTime(LocalDateTime.now());
//        // Lưu payment vào cơ sở dữ liệu
//        paymentService.save(payment);
//
//        // Tạo và trả về PaymentDto
//        PaymentDto paymentDto = new PaymentDto();
//        paymentDto.setOrderId(orderInfo);
//        paymentDto.setPaymentTime(paymentTime);
//        paymentDto.setTransactionId(transactionId);
//        paymentDto.setTotalPrice(totalPrice);
//        paymentDto.setBankCode(bankCode);
//        paymentDto.setResponseCode(responseCode);
//
//        // Cập nhật thông tin booking (nếu cần)
//        bookingService.update(booking);
//
//        // Trả về kết quả
//        return ResponseEntity.ok(paymentDto);
//    }

    @GetMapping("/order/payment/callback")
    public RedirectView getPaymentResult(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        // Lấy các tham số từ request
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");
        String responseCode = request.getParameter("vnp_ResponseCode");

        // Tìm Booking từ orderInfo
        Optional<Booking> optionalBooking = bookingService.getById(Integer.valueOf(orderInfo));
        if (!optionalBooking.isPresent()) {
            return new RedirectView("http://localhost:3000/en/checkout/failure?error=BookingNotFound");
        }

        Booking booking = optionalBooking.get();
        booking.setTotalPrice(Double.parseDouble(totalPrice));
        booking.setStatus("Paid");

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setTime(LocalDateTime.now());
        payment.setMethod("VNPay");
        payment.setStatus(paymentStatus == 1 ? "Paid" : "Failed");
        paymentService.save(payment);
        bookingService.update(booking);

        // Nếu thanh toán thành công, chuyển hướng tới success page
        if (paymentStatus == 1) {
            return new RedirectView("http://localhost:3000/en/checkout/success?orderId=" + orderInfo);
        } else {
            // Nếu thanh toán thất bại, chuyển hướng tới failure page
            return new RedirectView("http://localhost:3000/en/checkout/failure?orderId=" + orderInfo);
        }
    }
}
