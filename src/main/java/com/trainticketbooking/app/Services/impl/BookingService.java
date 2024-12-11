package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Booking.BookingDTO;
import com.trainticketbooking.app.Dtos.Booking.BookingRequestDTO;
import com.trainticketbooking.app.Dtos.Booking.BookingResponseDTO;
import com.trainticketbooking.app.Dtos.Passenger.PassengerResponseDTO;
import com.trainticketbooking.app.Dtos.Ticket.TicketDTO;
import com.trainticketbooking.app.Dtos.Ticket.TicketRequestDTO;
import com.trainticketbooking.app.Dtos.Ticket.TicketResponseDTO;
import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Repos.*;
import com.trainticketbooking.app.Services.IBookingService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private PassengerService passengerService;
    @Autowired
    private PassengerTypeService passengerTypeService;
    @Autowired
    private EmailService emailService;

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
            updatedBooking.setStatus(booking.getStatus());
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
        // List<TemporaryTicketHold> holds =
        // temporaryTicketHoldRepository.findAllById(holdIds);
        // Booking finalBooking = booking;
        // List<Ticket> tickets = holds.stream().map(hold -> {
        // Ticket ticket = new Ticket();
        // ticket.setBooking(finalBooking);
        //// ticket.setPassenger(hold.getSeat().getPassenger());
        // ticket.setPrice(1111.0);
        // ticket.setBookingDate(LocalDateTime.now());
        // ticket.setDepartureDate(hold.getDepartureDate());
        // ticket.setStatus("Paid");
        // ticket.setStartStation(hold.getDepartureStation());
        // ticket.setEndStation(hold.getArrivalStation());
        // ticket.setSeat(hold.getSeat());
        //// ticket.setTicketType();
        // return ticketRepository.save(ticket);
        // }).collect(Collectors.toList());
        //
        // // Xóa các TemporaryTicketHold sau khi tạo ticket
        // temporaryTicketHoldRepository.deleteAll(holds);

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
            // booking.setPaymentStatus("Paid"); // Có thể thêm trạng thái thanh toán cho
            // Booking nếu cần
            return booking;
        } else {
            throw new IllegalArgumentException("Invalid payment amount");
        }
    }
    @Transactional
    public Booking createBooking(BookingRequestDTO bookingDTO) {
        // Step 1: Create a new Booking entity using the details from BookingDTO

        bookingDTO.setStartStationId(stationRepository.findByCode(bookingDTO.getStartStationCode()).getStationId());
        bookingDTO.setEndStationId(stationRepository.findByCode(bookingDTO.getEndStationCode()).getStationId());
        Booking booking = new Booking();
        booking.setBookingTime(LocalDateTime.now());
        double totalPrice = 0;

        for (var ticketDTO : bookingDTO.getTickets()) {
            totalPrice += ticketDTO.getPrice();
            if (ticketDTO.getSeatReturnId() != 0) {
                totalPrice += ticketDTO.getSeatReturnPrice(); // Include return ticket price in total
            }
        }
        booking.setTotalPrice(totalPrice);

        // Get Station objects
        Station startStation = stationRepository.findById(bookingDTO.getStartStationId())
                .orElseThrow(() -> new RuntimeException("Start station not found"));
        Station endStation = stationRepository.findById(bookingDTO.getEndStationId())
                .orElseThrow(() -> new RuntimeException("End station not found"));

        booking.setStartStation(startStation);
        booking.setEndStation(endStation);
        booking.setDepartureDate(LocalDate.now());
        booking.setStatus("Pending");
        // Save the booking
        Booking savedBooking = bookingRepository.save(booking);

        // Step 2: Process each ticket in bookingDTO
        for (var ticketDTO : bookingDTO.getTickets()) {
            // Create outbound ticket
            Ticket outboundTicket = new Ticket();
            outboundTicket.setBooking(savedBooking);
            outboundTicket.setPrice(ticketDTO.getPrice());
            outboundTicket.setBookingDate(LocalDateTime.now());
            outboundTicket.setStatus("Booked");

            // Fetch seat object for outbound ticket
            Seat seat = seatRepository.findById(ticketDTO.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            outboundTicket.setSeat(seat);

            // Set departure status and station details
            outboundTicket.setDeparture(true);
            outboundTicket.setStartStation(startStation);
            outboundTicket.setEndStation(endStation);

            // Set passenger details
            var existedPassenger = passengerService
                    .getPassengerByIdentityCardNumber(ticketDTO.getPassenger().getIdentityCardNumber());

            if (existedPassenger.isEmpty()) {
                existedPassenger = Optional.of(new Passenger());
                var passengerType = passengerTypeService.getById(ticketDTO.getPassenger().getPassengerTypeId());
                existedPassenger.get().setIdentityCardNumber(ticketDTO.getPassenger().getIdentityCardNumber());
                existedPassenger.get().setPassengerType(passengerType.get());
            }
            existedPassenger.get().setFullName(ticketDTO.getPassenger().getFullName());
            outboundTicket.setPassenger(existedPassenger.get());
            passengerService.savePassenger(existedPassenger.get());

            // Save outbound ticket
            ticketRepository.save(outboundTicket);

            // If seatReturn is valid, create return ticket
            if (ticketDTO.getSeatReturnId() != 0) {
                Ticket returnTicket = new Ticket();
                returnTicket.setBooking(savedBooking);
                returnTicket.setPrice(ticketDTO.getSeatReturnPrice());
                returnTicket.setBookingDate(LocalDateTime.now());
                returnTicket.setStatus("Booked");
                returnTicket.setDepartureDate(bookingDTO.getArrivalDate());
                // Fetch seat object for return ticket
                Seat returnSeat = seatRepository.findById(ticketDTO.getSeatReturnId())
                        .orElseThrow(() -> new RuntimeException("Return seat not found"));
                returnTicket.setSeat(returnSeat);

                // Set departure status and station details (reverse for return trip)
                returnTicket.setDeparture(false);
                returnTicket.setStartStation(endStation); // Reverse start and end station
                returnTicket.setEndStation(startStation);

                // Use the same passenger as the outbound ticket
                returnTicket.setPassenger(existedPassenger.get());

                // Save return ticket
                ticketRepository.save(returnTicket);
            }
        }
        try {
////            var sendEmailBooking = bookingRepository.findByBookingId(savedBooking.getBookingId());
//            var sendEmailBooking = bookingRepository.findByBookingIdWithTickets(savedBooking.getBookingId())
//                    .orElseThrow(() -> new RuntimeException("Booking not found with tickets"));
            var tickets = ticketRepository.findByBookingBookingId(savedBooking.getBookingId());


            String emailBody = buildEmailContent(savedBooking, tickets);
            emailService.sendEmail("chrisnguyeen2000@gmail.com", "Booking Confirmation", emailBody, true);
        } catch (Exception e) {
            log.error("Failed to send booking confirmation email", e);
        }
        return savedBooking;
    }

    // Method to get booking details by ID
    public BookingResponseDTO getBookingById(Integer bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking == null) {
            return null; // Booking not found
        }

        // Convert Booking to BookingDTO
        BookingResponseDTO bookingDTO = new BookingResponseDTO();
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setBookingTime(booking.getBookingTime());
        bookingDTO.setTotalPrice(booking.getTotalPrice());
        bookingDTO.setStartStation(booking.getStartStation().getStationName());
        bookingDTO.setEndStation(booking.getEndStation().getStationName());
        bookingDTO.setDepartureDate(booking.getDepartureDate());
        bookingDTO.setStatus(booking.getStatus());
        // Add tickets associated with this booking
        List<TicketResponseDTO> ticketDTOs = ticketRepository.findByBookingBookingId(bookingId).stream()
                .map(ticket -> {
                    TicketResponseDTO ticketDTO = new TicketResponseDTO();
                    ticketDTO.setTicketId(ticket.getTicketId());
                    ticketDTO.setPrice(ticket.getPrice());
                    ticketDTO.setBookingDate(ticket.getBookingDate());
                    ticketDTO.setStatus(ticket.getStatus());
                    ticketDTO.setSeatNumber(ticket.getSeat().getSeatNumber());
                    ticketDTO.setTicketId(ticket.getSeat().getSeatId());

                    var passengerDto = new PassengerResponseDTO();
                    passengerDto.setPassengerId(ticket.getPassenger().getPassengerId());
                    passengerDto.setIdentityCardNumber(ticket.getPassenger().getIdentityCardNumber());
                    passengerDto.setFullName(ticket.getPassenger().getFullName());
                    passengerDto.setPassengerType(ticket.getPassenger().getPassengerType().getPassengerType());
                    ticketDTO.setPassenger(passengerDto);

                    return ticketDTO;
                })
                .toList();
        bookingDTO.setTickets(ticketDTOs);

        return bookingDTO;
    }

    // Method to get all bookings
    public List<BookingResponseDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(booking -> {
            BookingResponseDTO bookingDTO = new BookingResponseDTO();
            bookingDTO.setBookingId(booking.getBookingId());
            bookingDTO.setBookingTime(booking.getBookingTime());
            bookingDTO.setTotalPrice(booking.getTotalPrice());
            bookingDTO.setStartStation(booking.getStartStation().getStationName());
            bookingDTO.setEndStation(booking.getEndStation().getStationName());
            bookingDTO.setEndStation(booking.getEndStation().getStationName());
            bookingDTO.setDepartureDate(booking.getDepartureDate());
            bookingDTO.setStatus(booking.getStatus());
            List<TicketResponseDTO> ticketDTOs = ticketRepository.findByBookingBookingId(booking.getBookingId())
                    .stream()
                    .map(ticket -> {
                        TicketResponseDTO ticketDTO = new TicketResponseDTO();
                        ticketDTO.setTicketId(ticket.getTicketId());
                        ticketDTO.setPrice(ticket.getPrice());
                        ticketDTO.setBookingDate(ticket.getBookingDate());
                        ticketDTO.setStatus(ticket.getStatus());
                        ticketDTO.setSeatNumber(ticket.getSeat().getSeatNumber());
                        ticketDTO.setTicketId(ticket.getSeat().getSeatId());
                        ticketDTO.setTrainName(ticket.getSeat().getCarriage().getTrain().getTrainNumber());
                        ticketDTO.setCarriageName(ticket.getSeat().getCarriage().getCarriageNumber());
                        ticketDTO.setSeatType(ticket.getSeat().getSeatType().getCode());
                        ticketDTO.setStartStationName(ticket.getStartStation().getStationName());
                        ticketDTO.setEndStationName(ticket.getEndStation().getStationName());
                        ticketDTO.setDepartureDate(ticket.getDepartureDate());

                        var passengerDto = new PassengerResponseDTO();
                        passengerDto.setPassengerId(ticket.getPassenger().getPassengerId());
                        passengerDto.setIdentityCardNumber(ticket.getPassenger().getIdentityCardNumber());
                        passengerDto.setFullName(ticket.getPassenger().getFullName());
                        passengerDto.setPassengerType(ticket.getPassenger().getPassengerType().getPassengerType());
                        ticketDTO.setPassenger(passengerDto);

                        return ticketDTO;
                    })
                    .toList();
            bookingDTO.setTickets(ticketDTOs);
            return bookingDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<Booking> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Transactional
    private String buildEmailContent(Booking booking, List<Ticket> tickets) {
        StringBuilder emailContent = new StringBuilder();
        emailContent.append("<h1>Booking Confirmation</h1>");
        emailContent.append("<p>Thank you for your booking. Here are the details:</p>");

        emailContent.append("<h3>Booking Details:</h3>");
        emailContent.append("<p>Booking ID: ").append(booking.getBookingId()).append("</p>");
        emailContent.append("<p>Booking Time: ").append(booking.getBookingTime()).append("</p>");
        emailContent.append("<p>Departure Station: ").append(booking.getStartStation().getStationName()).append("</p>");
        emailContent.append("<p>Arrival Station: ").append(booking.getEndStation().getStationName()).append("</p>");
        emailContent.append("<p>Total Price: ").append(booking.getTotalPrice()).append("</p>");
        emailContent.append("<p>Status: ").append(booking.getStatus()).append("</p>");

        emailContent.append("<h3>Tickets:</h3>");
        for (Ticket ticket : tickets) {
            emailContent.append("<p>Ticket ID: ").append(ticket.getTicketId()).append("</p>");
            emailContent.append("<p>Passenger: ").append(ticket.getPassenger().getFullName()).append("</p>");
            emailContent.append("<p>Seat: ").append(ticket.getSeat().getSeatNumber()).append("</p>");
            emailContent.append("<p>Price: ").append(ticket.getPrice()).append("</p>");
            emailContent.append("<p>Departure: ").append(ticket.isDeparture() ? "Yes" : "No").append("</p>");
            emailContent.append("<hr>");
        }

        emailContent.append("<p>If you have any questions, feel free to contact us.</p>");
        emailContent.append("<p>Best regards,</p>");
        emailContent.append("<p>Your Company</p>");
        return emailContent.toString();
    }
}