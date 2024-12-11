package com.trainticketbooking.app.Controllers.API;

// Java version "1.8.0_201"
import com.trainticketbooking.app.Dtos.ZaloPayRequestDto;
import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.Payment;
import com.trainticketbooking.app.Services.IBookingService;
import com.trainticketbooking.app.Services.IPaymentService;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair; // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject; // https://mvnrepository.com/artifact/org.json/json
import com.trainticketbooking.app.vn.zalopay.crypto.HMACUtil; // tải về ở mục DOWNLOADS
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api/zalopay")
@Slf4j
public class ZalopayApiController {

    private static Map<String, String> config = new HashMap<String, String>(){{
        put("app_id", "2554");
        put("key1", "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn");
        put("key2", "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf");
        put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
        put("query_order_url", "https://sb-openapi.zalopay.vn/v2/query");
    }};

    @Autowired
    private IPaymentService paymentService;

    @Autowired
    private IBookingService bookingService;

    @PostMapping("/create")
    public ResponseEntity<?> createOrder(@RequestBody ZaloPayRequestDto zaloPayRequestDto) {
        String ngrokUrl = "https://abcd1234.ngrok.io";
        Payment payment = new Payment();
        Booking booking;
        Optional<Booking> bookingOptional = bookingService.getById(zaloPayRequestDto.getBookingId());
        if(bookingOptional.isPresent()){
            booking = bookingOptional.get();
            payment.setBooking(booking);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error",
                            "message", String.format("route has id = %d does not exist", zaloPayRequestDto.getBookingId())));
        }
        long time = System.currentTimeMillis();
        LocalDateTime localDateTime = Instant.ofEpochMilli(time)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        payment.setMethod("zalopay");
        payment.setStatus("pending");
        payment.setTime(localDateTime);
        try {
            Payment paymentResponse = paymentService.save(payment);

            final Map<String, Object> embed_data = Map.of("key", "value");
            final Map<String, Object>[] item = new Map[]{
                    Map.of("item_name", "product_name", "item_price", 1000)
            };
            double totalPrice = booking.getTotalPrice();
            Integer totalPriceInteger = (int) totalPrice;

            Map<String, Object> order = new HashMap<>() {{
                put("app_id", config.get("app_id"));
                put("app_trans_id", getCurrentTimeString("yyMMdd") + "_" + paymentResponse.getPaymentId());
                put("app_time", System.currentTimeMillis());
                put("app_user", zaloPayRequestDto.getUserId());
                put("amount", totalPriceInteger);
                put("description", "Pay for payment #" + paymentResponse.getPaymentId());
                put("bank_code", "");
                put("item", new JSONArray(Arrays.asList(item)).toString());
                put("embed_data", new JSONObject(embed_data).toString());
                put("callback_url", ngrokUrl + "/api/zalopay/callback");
            }};

            // Generate HMAC
            String data = order.get("app_id") + "|" + order.get("app_trans_id") + "|" + order.get("app_user") + "|" +
                    order.get("amount") + "|" + order.get("app_time") + "|" +
                    order.get("embed_data") + "|" + order.get("item");

            order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));

            // HTTP Client
            try (CloseableHttpClient client = HttpClients.createDefault()) {
                HttpPost post = new HttpPost(config.get("endpoint"));

                List<NameValuePair> params = new ArrayList<>();
                for (Map.Entry<String, Object> entry : order.entrySet()) {
                    params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }

                // Set request entity
                post.setEntity(new UrlEncodedFormEntity(params));
                post.setHeader("Content-Type", "application/x-www-form-urlencoded");

                // Execute request
                try (CloseableHttpResponse response = client.execute(post)) {
                    if (response.getStatusLine().getStatusCode() != 200) {
                        return ResponseEntity.status(response.getStatusLine().getStatusCode())
                                .body("Error: Failed to create order");
                    }

                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    StringBuilder resultJsonStr = new StringBuilder();
                    String line;
                    while ((line = rd.readLine()) != null) {
                        resultJsonStr.append(line);
                    }

                    JSONObject result = new JSONObject(resultJsonStr.toString());
                    log.info("Request Params: " + order);
                    log.info("Response: " + resultJsonStr);
                    return ResponseEntity.ok(result.toMap());
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal Server Error", "message", e.toString()));
        }
    }

    @PostMapping("/callback")
    public String callback(@RequestBody String jsonStr) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac HmacSHA256 = Mac.getInstance("HmacSHA256");
        HmacSHA256.init(new SecretKeySpec(config.get("key2").getBytes(), "HmacSHA256"));

        JSONObject result = new JSONObject();
        try {
            JSONObject cbdata = new JSONObject(jsonStr);
            String dataStr = cbdata.getString("data");
            String reqMac = cbdata.getString("mac");

            byte[] hashBytes = HmacSHA256.doFinal(dataStr.getBytes());
            String mac = DatatypeConverter.printHexBinary(hashBytes).toLowerCase();

            // kiểm tra callback hợp lệ (đến từ ZaloPay server)
            if (!reqMac.equals(mac)) {
                // callback không hợp lệ
                result.put("return_code", -1);
                result.put("return_message", "mac not equal");
            } else {
                // thanh toán thành công
                // merchant cập nhật trạng thái cho đơn hàng
                JSONObject data = new JSONObject(dataStr);
                String appTransId = data.getString("app_trans_id");
                log.info("update order's status = success where app_trans_id = " + appTransId);
                String[] parts = appTransId.split("_");
                Integer paymentId = Integer.parseInt(parts[1]); // Chuyển thành số nguyên
                Optional<Payment> paymentOptional = paymentService.getById(paymentId);
                if(paymentOptional.isPresent()){
                    Payment payment = paymentOptional.get();
                    payment.setStatus("paid");
                    paymentService.save(payment);
                }

                result.put("return_code", 1);
                result.put("return_message", "success");
            }
            log.info("Received callback with data: " + jsonStr);
            log.info("Calculated mac: " + mac + ", reqMac: " + reqMac);
        } catch (Exception ex) {
            log.error("Received callback with data: " + ex.toString());
            result.put("return_code", 0); // ZaloPay server sẽ callback lại (tối đa 3 lần)
            result.put("return_message", ex.getMessage());
        }

        // thông báo kết quả cho ZaloPay server
        return result.toString();
    }

    @PostMapping("/status")
    public ResponseEntity<?> status(@RequestBody String app_trans_id) throws URISyntaxException, IOException {
        //app_trans_id truyền vào từ swager ko có dấu ""

        // Tạo dữ liệu để tính toán HMAC
        String data = config.get("app_id") + "|" + app_trans_id + "|" + config.get("key1"); // app_id|app_trans_id|key1
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data);

        // Tạo danh sách tham số
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app_id", config.get("app_id")));
        params.add(new BasicNameValuePair("app_trans_id", app_trans_id));
        params.add(new BasicNameValuePair("mac", mac));

        // Ghi log các tham số
        for (NameValuePair param : params) {
            System.out.println(param.getName() + " = " + param.getValue());
        }

        // Xây dựng URI từ endpoint
        URIBuilder uri = new URIBuilder(config.get("query_order_url"));
        uri.addParameters(params);

        // Gửi yêu cầu HTTP POST
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri.build());
        post.setEntity(new UrlEncodedFormEntity(params));

        // Nhận phản hồi từ API
        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        // Đọc kết quả từ phản hồi
        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        // Chuyển kết quả thành đối tượng JSON
        JSONObject result = new JSONObject(resultJsonStr.toString());

        // Trả về kết quả JSON dưới dạng ResponseEntity
        return ResponseEntity.ok(result.toString());
    }


    private static String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }
}
