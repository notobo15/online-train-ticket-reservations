package com.trainticketbooking.app.Utils;


import jakarta.servlet.http.HttpServletRequest;

public class UrlUtils {

    public static String getBaseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }
}