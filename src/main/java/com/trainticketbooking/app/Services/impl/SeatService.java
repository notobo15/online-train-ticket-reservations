package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.SeatDto;
import com.trainticketbooking.app.Repos.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    // Lấy danh sách ghế theo mã tàu
    // public List<SeatDto> getSeats(Long trainId) {
    // // Tìm kiếm ghế từ cơ sở dữ liệu
    // return seatRepository.findByTrainId(trainId);
    // }
}