//package com.trainticketbooking.app.Controllers.API;
//
//import com.trainticketbooking.app.Configs.PayPalHttpClient;
//import com.trainticketbooking.app.Dtos.OrderDTO;
//import com.trainticketbooking.app.Dtos.OrderResponseDto;
//import com.trainticketbooking.app.Dtos.PayPalAppContextDTO;
//import com.trainticketbooking.app.Entities.Order;
//import com.trainticketbooking.app.Repos.OrderRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping(value = "/checkout")
//@Slf4j
//public class CheckoutApiController {
//    @Autowired
//    private PayPalHttpClient payPalHttpClient;
//    @Autowired
//    private OrderRepository orderDAO;
//
//    @PostMapping
//    public ResponseEntity<OrderResponseDto> checkout(@RequestBody OrderDTO orderDTO) throws Exception {
//        var appContext = new PayPalAppContextDTO();
//        appContext.setReturnUrl("http://localhost:8080/checkout/success");
//        appContext.setBrandName("My brand");
//        appContext.setLandingPage(PaymentLandingPage.BILLING);
//        orderDTO.setApplicationContext(appContext);
//        var orderResponse = payPalHttpClient.createOrder(orderDTO);
//
//        var entity = new Order();
//        entity.setPaypalOrderId(orderResponse.getId());
//        entity.setPaypalOrderStatus(orderResponse.getStatus().toString());
//        var out = orderDAO.save(entity);
//        return ResponseEntity.ok(orderResponse);
//    }
//
//    @GetMapping(value = "/success")
//    public ResponseEntity paymentSuccess(PayPalHttpClient request) {
//        var orderId = request.getParameter("token");
//        var out = orderDAO.findByPaypalOrderId(orderId);
//        out.setPaypalOrderStatus(OrderStatus.APPROVED.toString());
//        orderDAO.save(out);
//        return ResponseEntity.ok().build();
//    }
//}
