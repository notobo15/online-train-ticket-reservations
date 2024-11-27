package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Ticket.TicketDTO;
import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Mappers.TicketMapper;
import com.trainticketbooking.app.Requests.TicketRequestDTO;
import com.trainticketbooking.app.Responses.ApiResponse;
import com.trainticketbooking.app.Services.ITicketService;
import com.trainticketbooking.app.Services.impl.CarriageSeatMappingService;
import com.trainticketbooking.app.Services.impl.SeatService;
import com.trainticketbooking.app.Services.impl.StationService;
import com.trainticketbooking.app.Services.impl.TicketTypeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
@AllArgsConstructor
public class TicketController {

    private ITicketService ticketService;
    private StationService stationService;
    private SeatService seatService;
    private TicketMapper ticketMapper;
    private TicketTypeService ticketTypeService;

    @PostMapping("/hold")
    public ResponseEntity<ApiResponse<Ticket>> holdTicket(@RequestParam Long ticketId) {
        Optional<Ticket> optionalTicket = ticketService.getById(ticketId.intValue());
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Kiểm tra xem vé có đang trống không
            if (ticket.getStatus().equals("Trống")) {
                // Đặt trạng thái "Đang giữ chỗ" và cập nhật thời gian giữ chỗ
                ticket.setStatus("Đang giữ chỗ");
                ticket.setBookingDate(LocalDateTime.now());
                Ticket updatedTicket = ticketService.save(ticket);

                return ResponseEntity.ok(new ApiResponse<>(true, "Vé đã được giữ chỗ thành công.", updatedTicket));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>(false, "Vé đã được đặt hoặc đang giữ chỗ.", null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Không tìm thấy vé.", null));
        }
    }

    @PostMapping("/hold1")
    public ResponseEntity<ApiResponse<TicketDTO>> holdTicket1(@RequestBody TicketRequestDTO request) {
        // Kiểm tra tính hợp lệ của ga xuất phát và ga đến
        Station startStation = stationService.getById(request.getStartStationId())
                .orElseThrow(() -> new EntityNotFoundException("Start station not found"));
        Station endStation = stationService.getById(request.getEndStationId())
                .orElseThrow(() -> new EntityNotFoundException("End station not found"));

        // Kiểm tra tính hợp lệ của chỗ ngồi
        Seat seat = seatService.getById(request.getSeatId())
                .orElseThrow(() -> new EntityNotFoundException("Seat not found"));

        // Tạo vé mới với trạng thái "Đang giữ chỗ"
        Ticket ticket = new Ticket();
        ticket.setStartStation(startStation);
        ticket.setEndStation(endStation);
        ticket.setPrice(99999D);
        ticket.setDepartureDate(request.getDepartureDate());
        ticket.setSeat(seat);
        ticket.setStatus("Đang giữ chỗ");
        ticket.setBookingDate(LocalDateTime.now());

        // Lưu vé vào cơ sở dữ liệu
        Ticket savedTicket = ticketService.save(ticket);

        // Chuyển đổi sang DTO bằng MapStruct
        TicketDTO ticketDTO = ticketMapper.toDTO(savedTicket);

        // Trả về thông tin vé đã giữ dưới dạng DTO
        return ResponseEntity.ok(new ApiResponse<>(true, "Vé đã được giữ chỗ thành công.", ticketDTO));
    }
    @PostMapping("/pay")
    public ResponseEntity<ApiResponse<TicketDTO>> payForTicket(@RequestParam Long ticketId) {
        Optional<Ticket> optionalTicket = ticketService.getById(ticketId.intValue());
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();

            // Kiểm tra nếu vé đang ở trạng thái "Đang giữ chỗ"
            if (ticket.getStatus().equals("Đang giữ chỗ")) {
                ticket.setStatus("Đã đặt");
                Ticket paidTicket = ticketService.save(ticket);

                // Chuyển đổi sang DTO bằng MapStruct
                TicketDTO ticketDTO = ticketMapper.toDTO(paidTicket);

                // Trả về thông tin vé đã được thanh toán dưới dạng DTO
                return ResponseEntity.ok(new ApiResponse<>(true, "Thanh toán thành công, vé đã được đặt.", ticketDTO));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ApiResponse<>(false, "Vé không ở trạng thái giữ chỗ, không thể thanh toán.", null));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Không tìm thấy vé.", null));
        }
    }

    @PostMapping("/createTicket")
    public ResponseEntity<ApiResponse<TicketDTO>> createTicket(@RequestBody TicketRequestDTO request) {
        // Tìm loại vé "Khứ hồi" trong bảng TicketType
        TicketType ticketType = ticketTypeService.findByName(request.getTicketTypeName())
                .orElseThrow(() -> new EntityNotFoundException("Ticket type not found"));

        // Tạo vé mới
        Ticket ticket = new Ticket();
        ticket.setStartStation(stationService.getById(request.getStartStationId()).get());
        ticket.setEndStation(stationService.getById(request.getEndStationId()).get());
        ticket.setPrice(request.getPrice());
        ticket.setDepartureDate(request.getDepartureDate());
        ticket.setSeat(seatService.getById(request.getSeatId()).get());
        ticket.setBookingDate(LocalDateTime.now());
        ticket.setStatus("Đang giữ chỗ");

        // Lưu vé vào cơ sở dữ liệu
        Ticket savedTicket = ticketService.save(ticket);

        // Chuyển đổi sang DTO và trả về kết quả
        TicketDTO ticketDTO = ticketMapper.toDTO(savedTicket);
        return ResponseEntity.ok(new ApiResponse<>(true, "Vé đã được tạo thành công.", ticketDTO));
    }
}