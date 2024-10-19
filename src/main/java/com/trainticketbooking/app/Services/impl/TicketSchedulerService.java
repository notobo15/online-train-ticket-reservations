package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Services.ITicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketSchedulerService {

    @Autowired
    private ITicketService ticketService;

    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi 60 giây
    public void cancelUnpaidTickets() {
        List<Ticket> tickets = ticketService.getAll();
        LocalDateTime now = LocalDateTime.now();

        for (Ticket ticket : tickets) {
            if (ticket.getStatus().equals("Đang giữ chỗ")) {
                // Kiểm tra nếu vé đã giữ quá 15 phút
                if (ticket.getBookingDate().plusMinutes(15).isBefore(now)) {
                    ticket.setStatus("Trống"); // Giải phóng vé
                    ticketService.save(ticket);
                    System.out.println("Vé " + ticket.getTicketId() + " đã được giải phóng do không thanh toán.");
                }
            }
        }
    }
}