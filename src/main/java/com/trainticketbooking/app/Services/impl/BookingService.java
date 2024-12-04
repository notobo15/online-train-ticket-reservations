package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Repos.BookingRepository;
import com.trainticketbooking.app.Repos.TicketRepository;
import com.trainticketbooking.app.Repos.UserRepository;
import com.trainticketbooking.app.Services.IBookingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> getById(Integer id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public void deleteById(Integer id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public Booking update(Booking booking) {
        Optional<Booking> existingBooking = bookingRepository.findById(booking.getBookingId());
        if (existingBooking.isPresent()) {
            Booking updatedBooking = existingBooking.get();
            updatedBooking.setBookingTime(booking.getBookingTime());
            updatedBooking.setTotalPrice(booking.getTotalPrice());
            return bookingRepository.save(updatedBooking);
        } else {
            throw new RuntimeException("Booking not found with ID: " + booking.getBookingId());
        }
    }

    @Transactional
    public Booking createBookingFromHolds(Integer userId, List<Integer> holdIds, Double totalPrice) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Booking booking = new Booking();
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalPrice(totalPrice);
        booking.setUser(user);
        booking = bookingRepository.save(booking);

        // Lấy danh sách hold và chuyển đổi thành ticket
//        List<TemporaryTicketHold> holds = temporaryTicketHoldRepository.findAllById(holdIds);
//        Booking finalBooking = booking;
//        List<Ticket> tickets = holds.stream().map(hold -> {
//            Ticket ticket = new Ticket();
//            ticket.setBooking(finalBooking);
////            ticket.setPassenger(hold.getSeat().getPassenger());
//            ticket.setPrice(1111.0);
//            ticket.setBookingDate(LocalDateTime.now());
//            ticket.setDepartureDate(hold.getDepartureDate());
//            ticket.setStatus("Paid");
//            ticket.setStartStation(hold.getDepartureStation());
//            ticket.setEndStation(hold.getArrivalStation());
//            ticket.setSeat(hold.getSeat());
////            ticket.setTicketType();
//            return ticketRepository.save(ticket);
//        }).collect(Collectors.toList());
//
//        // Xóa các TemporaryTicketHold sau khi tạo ticket
//        temporaryTicketHoldRepository.deleteAll(holds);

        return booking;
    }

    // Xác nhận thanh toán
    @Transactional
    public Booking confirmPayment(Integer bookingId, Double amount) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getTotalPrice().equals(amount)) {
            // Thay đổi trạng thái của vé sang "Paid"
            for (Ticket ticket : booking.getTickets()) {
                ticket.setStatus("Paid");
                ticketRepository.save(ticket);
            }
            // Thêm logic thanh toán và cập nhật trạng thái thanh toán
            // booking.setPaymentStatus("Paid"); // Có thể thêm trạng thái thanh toán cho Booking nếu cần
            return booking;
        } else {
            throw new IllegalArgumentException("Invalid payment amount");
        }
    }

    @Override
    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }
}