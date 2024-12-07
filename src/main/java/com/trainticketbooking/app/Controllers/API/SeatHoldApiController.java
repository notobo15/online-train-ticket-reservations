        package com.trainticketbooking.app.Controllers.API;

        import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
        import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldResponseDto;
        import com.trainticketbooking.app.Dtos.Wrappers.ApiResponse;
        import com.trainticketbooking.app.Services.impl.SeatHoldService;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.*;

        @RestController
        @RequestMapping("/api/seatholds")
        public class SeatHoldApiController {

            private final SeatHoldService seatHoldService;

            @Autowired
            public SeatHoldApiController(SeatHoldService seatHoldService) {
                this.seatHoldService = seatHoldService;
            }

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
        }