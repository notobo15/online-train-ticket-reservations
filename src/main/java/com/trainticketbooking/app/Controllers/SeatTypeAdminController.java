package com.trainticketbooking.app.Controllers;


import com.trainticketbooking.app.Entities.SeatType;
import com.trainticketbooking.app.Services.ISeatTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/seat-types")
public class SeatTypeAdminController {

    @Autowired
    private ISeatTypeService mSeatTypeService;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<SeatType>> getAllSeatTypes() {
        List<SeatType> seatTypes = mSeatTypeService.getAll();
        if (seatTypes.isEmpty()) {
            return ResponseEntity.noContent().build();  // HTTP 204 nếu danh sách trống
        }
        return ResponseEntity.ok(seatTypes);  // HTTP 200 với dữ liệu
    }
}
