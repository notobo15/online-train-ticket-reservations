package com.trainticketbooking.app.Controllers.API.Auth;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.trainticketbooking.app.Dtos.GoogleIdToken;
import com.trainticketbooking.app.Dtos.TokenRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.interfaces.RSAKey;
import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private static final String GOOGLE_PUBLIC_KEYS_URL = "https://www.googleapis.com/oauth2/v3/certs"; // URL chứa các public key của Google

    @PostMapping("/login-with-google")
    public String handleGoogleLogin(@RequestBody GoogleIdToken googleIdToken) {
        // Trích xuất thông tin từ GoogleIdToken
        String email = googleIdToken.getEmail();
        String name = googleIdToken.getName();
        String picture = googleIdToken.getPicture();

        // Kiểm tra thông tin từ token (có thể xác minh nếu cần)
        if (googleIdToken.isEmail_verified()) {
            // Token hợp lệ và email đã được xác thực
            return String.format("Login successful! Name: %s, Email: %s, Picture: %s", name, email, picture);
        } else {
            // Email chưa được xác thực
            return "Email is not verified.";
        }
    }


}
