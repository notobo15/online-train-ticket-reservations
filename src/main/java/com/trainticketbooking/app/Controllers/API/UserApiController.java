package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.SetPasswordRequest;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.impl.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class UserApiController {


    @Autowired
    private UserService userService;

    //    @GetMapping("/user")
//    public ResponseEntity<Map<String, Object>> getUserInfo(Authentication authentication) {
//        // Giả sử bạn đang cố gắng lấy thông tin người dùng từ authentication
//        if (authentication == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
//        }
//
//        // Lấy thông tin từ user hoặc access token
//        Object principal = authentication.getPrincipal();
//        if (principal == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "No user info found"));
//        }
//
//        // Tiến hành xử lý thông tin người dùng
//        UserDetails userDetails = (UserDetails) principal; // Ví dụ nếu dùng UserDetails
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("username", userDetails.getUsername());
//
//        return ResponseEntity.ok(userInfo);
//    }
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/user/set-password")
    public ResponseEntity<String> setPassword(@RequestBody SetPasswordRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> userOptional = userService.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Mã hóa mật khẩu trước khi lưu
            user.setPassword(passwordEncoder.encode(password));

            userService.save(user);  // Lưu người dùng với mật khẩu đã thay đổi

            return ResponseEntity.ok("Password set successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }


}