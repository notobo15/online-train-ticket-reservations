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

    // Create a new booking
    public Booking createBooking(BookingRequestDTO bookingDTO) {
        // Step 1: Create a new Booking entity using the details from BookingDTO
        Booking booking = new Booking();
        booking.setBookingTime(LocalDateTime.now());
        double totalPrice = 0;
        for (var ticketDTO : bookingDTO.getTickets()) {
            totalPrice += ticketDTO.getPrice();
        }
        booking.setTotalPrice(totalPrice);

        // Assuming you are passing station IDs, you can get the actual Station objects
        // if needed
        Station startStation = stationRepository.findById(bookingDTO.getStartStationId())
                .orElseThrow(() -> new RuntimeException("Start station not found"));
        Station endStation = stationRepository.findById(bookingDTO.getEndStationId())
                .orElseThrow(() -> new RuntimeException("End station not found"));

        booking.setStartStation(startStation);
        booking.setEndStation(endStation);
        booking.setDepartureDate(LocalDate.now());

        // Save the booking
        Booking savedBooking = bookingRepository.save(booking);

        // Step 2: For each TicketDTO, create and associate the ticket with the booking
        for (var ticketDTO : bookingDTO.getTickets()) {
            Ticket ticket = new Ticket();
            ticket.setBooking(savedBooking);
            ticket.setPrice(ticketDTO.getPrice());
            ticket.setBookingDate(LocalDateTime.now());
            ticket.setStatus("Booked");

            // Fetch seat object using seatId
            Seat seat = seatRepository.findById(ticketDTO.getSeatId())
                    .orElseThrow(() -> new RuntimeException("Seat not found"));
            ticket.setSeat(seat);

            // Set departure status
            ticket.setDeparture(ticketDTO.isDeparture());

            // Save the ticket
            ticketRepository.save(ticket);

            var existedPassenger = passengerService
                    .getPassengerByIdentityCardNumber(ticketDTO.getPassenger().getIdentityCardNumber());

            if (existedPassenger.isPresent()) {

            } else {

                existedPassenger = Optional.of(new Passenger());
                var passengerType = passengerTypeService.getById(ticketDTO.getPassenger().getPassengerTypeId());
                existedPassenger.get().setIdentityCardNumber(ticketDTO.getPassenger().getIdentityCardNumber());
                existedPassenger.get().setPassengerType(passengerType.get());
            }
            existedPassenger.get().setFullName(ticketDTO.getPassenger().getFullName());
            passengerService.savePassenger(existedPassenger.get());
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
            bookingDTO.setDepartureDate(booking.getDepartureDate());
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
}