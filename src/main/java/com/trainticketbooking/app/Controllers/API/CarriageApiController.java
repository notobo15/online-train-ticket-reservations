package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldResponseDto;
import com.trainticketbooking.app.Dtos.Wrappers.ApiResponse;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Exceptions.ErrorCode;
import com.trainticketbooking.app.Services.impl.CarriageService;
import com.trainticketbooking.app.Services.impl.SeatHoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carriages")
public class CarriageApiController {

    @Autowired
    private CarriageService carriageService;
    @Autowired
    private SeatHoldService seatHoldService;
    // @GetMapping("/by-train/{trainId}")
    // public ResponseEntity<List<CarriageDTO>> searchCarriagesByTrain(@PathVariable
    // Integer trainId) {
    // List<CarriageDTO> result = carriageService.findByTrainTrainId(trainId);
    // return ResponseEntity.ok(result);
    // }

    // @MessageMapping("/getSeatsByCarriage")
    // @SendTo("/topic/seats")
    // @GetMapping("/{carriageId}/seats")
    // public List<CarriageSeatMappingDTO> getSeatsByCarriage(@PathVariable Integer
    // carriageId) {
    // return carriageService.getSeatMappings(carriageId);
    // }

    @GetMapping("/{carriageId}")
    public ApiResponse<CarriageDTO> getCarriageById(@PathVariable Integer carriageId) {
        // Lấy thông tin của Carriage
        CarriageDTO carriage = carriageService.getCarriageById(carriageId);

        // Lấy số lượng chỗ ngồi của Carriage
        int seatCount = carriageService.getSeatCountByCarriageId(carriageId);
        carriage.setSeatCount(seatCount); // Giả sử CarriageDTO có thuộc tính seatCount

        return ApiResponse.<CarriageDTO>builder()
                .result(carriage)
                .build();
    }

    @GetMapping("/{carriageId}/seats")
    public ApiResponse<CarriageDTO> getCarriageWithSeatsById(@PathVariable Integer carriageId, SeatHoldRequestDto dto) {
        // Lấy thông tin của Carriage từ dịch vụ
        Optional<Carriage> carriageOptional = carriageService.getById(carriageId);

        if (carriageOptional.isEmpty()) {
            return ApiResponse.<CarriageDTO>builder()
                    .result(null)
                    .success(false)
                    .message("Khong tim thay")
                    .build();
        }
        var seats = seatHoldService.getListSeats(dto);

        var carriage = carriageOptional.get();
        CarriageDTO carriageDTO = new CarriageDTO();
        carriageDTO.setCarriageId(carriage.getCarriageId());
        carriageDTO.setCarriageNumber(carriage.getCarriageNumber());
        carriageDTO.setCarriageClassName(carriage.getCarriageClass().getName());
        carriageDTO.setSeats(seats);

        return ApiResponse.<CarriageDTO>builder()
                .result(carriageDTO)
                .success(true)
                .build();
    }

    @MessageMapping("/seats")
    @SendTo("/topic/seats")
    public ApiResponse<CarriageDTO> getCarriageWithSeatsById(SeatHoldRequestDto dto) {
        // Lấy thông tin của Carriage từ dịch vụ
        Optional<Carriage> carriageOptional = carriageService.getById(dto.getCarriageId());

        if (carriageOptional.isEmpty()) {
            return ApiResponse.<CarriageDTO>builder()
                    .result(null)
                    .success(false)
                    .message("Khong tim thay")
                    .build();
        }
        var seats = seatHoldService.getListSeats(dto);

        var carriage = carriageOptional.get();
        CarriageDTO carriageDTO = new CarriageDTO();
        carriageDTO.setCarriageId(carriage.getCarriageId());
        carriageDTO.setCarriageNumber(carriage.getCarriageNumber());
        carriageDTO.setCarriageClassName(carriage.getCarriageClass().getName());
        carriageDTO.setSeats(seats);

        return ApiResponse.<CarriageDTO>builder()
                .result(carriageDTO)
                .success(true)
                .build();
    }

    @MessageMapping("/seats/{carriageId}")
    @SendTo("/topic/seats/{carriageId}")
    public ApiResponse<CarriageDTO> getCarriageWithSeatsById(@DestinationVariable int carriageId,
            SeatHoldRequestDto dto) {
        // Lấy thông tin của Carriage từ dịch vụ
        Optional<Carriage> carriageOptional = carriageService.getById(carriageId);

        if (carriageOptional.isEmpty()) {
            return ApiResponse.<CarriageDTO>builder()
                    .result(null)
                    .success(false)
                    .message("Khong tim thay")
                    .build();
        }
        var seats = seatHoldService.getListSeats(dto);

        var carriage = carriageOptional.get();
        CarriageDTO carriageDTO = new CarriageDTO();
        carriageDTO.setCarriageId(carriage.getCarriageId());
        carriageDTO.setCarriageNumber(carriage.getCarriageNumber());
        carriageDTO.setCarriageClassName(carriage.getCarriageClass().getName());
        carriageDTO.setSeats(seats);

        return ApiResponse.<CarriageDTO>builder()
                .result(carriageDTO)
                .success(true)
                .build();
    }
}
