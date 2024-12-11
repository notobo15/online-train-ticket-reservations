package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldResponseDto;
import com.trainticketbooking.app.Dtos.SeatHolds.WebSocketResponse;
import com.trainticketbooking.app.Dtos.Wrappers.ApiResponse;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Services.impl.CarriageService;
import com.trainticketbooking.app.Services.impl.SeatHoldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/seatholds")
public class SeatHoldApiController {

    @Autowired
    private SeatHoldService seatHoldService;

    @Autowired
    private CarriageService carriageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Endpoint to create a seat hold
    @PostMapping
    public ApiResponse<SeatHoldResponseDto> createSeatHold(@RequestBody CreateSeatHoldRequestDto seatHoldRequestDto) {
        var seathold = seatHoldService.createSeatHold(seatHoldRequestDto);
        if (seathold != null) {
            return ApiResponse.<SeatHoldResponseDto>builder()
                    .result(seathold)
                    .success(true)
                    .message("SeatHold created successfully.")
                    .build();
        } else {
            return ApiResponse.<SeatHoldResponseDto>builder()
                    .result(null)
                    .success(false)
                    .message("Can't create seat hold successfully.")
                    .build();
        }

    }

    @DeleteMapping("/{id}")
    public ApiResponse<SeatHoldResponseDto> deleteSeatHold(@PathVariable Integer id) {
        try {
            seatHoldService.deleteSeatHoldById(id);

            return ApiResponse.<SeatHoldResponseDto>builder()
                    .result(null)
                    .success(true)
                    .message("SeatHold with ID " + id + " has been deleted.")
                    .build();
        } catch (RuntimeException ex) {
            return ApiResponse.<SeatHoldResponseDto>builder()
                    .result(null)
                    .success(false)
                    .message(ex.getMessage())
                    .build();
        }
    }

    @MessageMapping("/hold")
    @SendTo("/topic/seats")
    public ApiResponse<CarriageDTO> createSeatHoldWS(SeatHoldRequestDto request) {
        var dto = new CreateSeatHoldRequestDto();
        dto.setSeatId(request.getSeatId());
        dto.setTrainId(request.getTrainId());
        dto.setDepartureDate(request.getDepartureDate());

        dto.setArrivalStationCode(request.getArrivalStationCode());
        dto.setDepartureStationCode(request.getDepartureStationCode());

        var seathold = seatHoldService.createSeatHold(dto);
        Optional<Carriage> carriageOptional = carriageService.getById(request.getCarriageId());

        if (carriageOptional.isEmpty()) {
            return ApiResponse.<CarriageDTO>builder()
                    .result(null)
                    .success(false)
                    .message("Khong tim thay")
                    .build();
        }
        var seats = seatHoldService.getListSeats(request);

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


    @MessageMapping("/delete-hold")
    @SendTo("/topic/seats")
    public ApiResponse<CarriageDTO> deleteSeatHoldWS(SeatHoldRequestDto request) {


        var dto = new CreateSeatHoldRequestDto();
        dto.setSeatId(request.getSeatId());
        dto.setDepartureDate(request.getDepartureDate());
        dto.setArrivalStationCode(request.getArrivalStationCode());
        dto.setDepartureStationCode(request.getDepartureStationCode());
        dto.setTrainId(request.getTrainId());

        seatHoldService.deleteSeatHold(dto);

        Optional<Carriage> carriageOptional = carriageService.getById(request.getCarriageId());

        if (carriageOptional.isEmpty()) {
            return ApiResponse.<CarriageDTO>builder()
                    .result(null)
                    .success(false)
                    .message("Khong tim thay")
                    .build();
        }
        var seats = seatHoldService.getListSeats(request);


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

//
//    @MessageMapping("/hold")
//    @SendTo("/topic/seats")
//    public ApiResponse<SeatHoldResponseDto> createSeatHoldWS(CreateSeatHoldRequestDto seatHoldRequestDto) {
//        var seathold = seatHoldService.createSeatHold(seatHoldRequestDto);
//        if (seathold != null) {
//            return ApiResponse.<SeatHoldResponseDto>builder()
//                    .result(seathold)
//                    .success(true)
//                    .message("SeatHold created successfully.")
//                    .build();
//        } else {
//            return ApiResponse.<SeatHoldResponseDto>builder()
//                    .result(null)
//                    .success(false)
//                    .message("Can't create seat hold successfully.")
//                    .build();
//        }
//
//    }
//
//
//
//    @MessageMapping("/delete-hold")
//    @SendTo("/topic/seats")
//    public ApiResponse<SeatHoldResponseDto> deleteSeatHoldWS(CreateSeatHoldRequestDto seatHoldRequestDto) {
//        try {
//            seatHoldService.deleteSeatHold(seatHoldRequestDto);
//
//            return ApiResponse.<SeatHoldResponseDto>builder()
//                    .result(null)
//                    .success(true)
////                    .message("SeatHold with ID " + id + " has been deleted.")
//                    .build();
//        } catch (RuntimeException ex) {
//            return ApiResponse.<SeatHoldResponseDto>builder()
//                    .result(null)
//                    .success(false)
//                    .message(ex.getMessage())
//                    .build();
//        }
//    }


    private List<String> messages = new ArrayList<>();

    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public List<String> getListMessage() {
        return messages;
    }

    @MessageMapping("/chat")  // Client sẽ gửi tin nhắn tới /app/chat
    @SendTo("/topic/messages")  // Gửi tin nhắn tới tất cả các client đang subscribe tại /topic/messages
    public List<String> sendMessage(String message) {
        messages.add(message);
        return messages;
    }

    @MessageMapping("/deleteMessage") // Nhận yêu cầu xóa tin nhắn
    @SendTo("/topic/messages")  // Gửi lại danh sách tin nhắn sau khi xóa
    public List<String> deleteMessage(Integer messageIndex) {
        if (messageIndex != null && messageIndex >= 0 && messageIndex < messages.size()) {
            messages.remove((int) messageIndex); // Xóa tin nhắn theo index
        }
        return messages; // Trả về danh sách tin nhắn còn lại sau khi xóa
    }



}