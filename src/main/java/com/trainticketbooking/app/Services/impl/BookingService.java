package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Booking;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Repos.BookingRepository;
import com.trainticketbooking.app.Services.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;


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
}