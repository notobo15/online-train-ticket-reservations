package com.trainticketbooking.app.Dtos.SeatHolds;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WebSocketResponse {
    private boolean success;
    private Object data; // Đây có thể là danh sách ghế hoặc thông báo lỗi
}