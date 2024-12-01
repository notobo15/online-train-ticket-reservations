package com.trainticketbooking.app.Dtos;

import lombok.Data;

@Data
public class GoogleIdToken {
    private String iss;          // "https://accounts.google.com"
    private String azp;          // "145680772414-q8roodq6j4ghob4mutu10l99fv1li54h.apps.googleusercontent.com"
    private String aud;          // "145680772414-q8roodq6j4ghob4mutu10l99fv1li54h.apps.googleusercontent.com"
    private String sub;          // "110752842555105665760"
    private String email;        // "chrisnguyeen2000@gmail.com"
    private boolean email_verified;  // true
    private long nbf;            // 1732973486
    private String name;         // "Texxt"
    private String picture;      // URL của ảnh
    private String given_name;   // "Texxt"
    private long iat;            // 1732973786
    private long exp;            // 1732977386
    private String jti;          // "478d1618913de6c93ecc8ca71b4920cfb8396359"
}