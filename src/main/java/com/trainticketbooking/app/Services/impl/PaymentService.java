package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Payment;
import com.trainticketbooking.app.Repos.PaymentRepository;
import com.trainticketbooking.app.Services.IPaymentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService implements IPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;


    @Override
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Optional<Payment> getById(Integer id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void deleteById(Integer id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public Payment update(Payment payment) {
        Payment existingPayment= paymentRepository.findById(payment.getPaymentId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with ID: " + payment.getPaymentId()));
        existingPayment.setStatus(payment.getStatus());
        return paymentRepository.save(existingPayment);
    }
}
