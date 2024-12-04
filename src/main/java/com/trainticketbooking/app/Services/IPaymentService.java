package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Payment;
import com.trainticketbooking.app.Entities.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPaymentService extends IService<Payment>{
    Page<Payment> findAll(Pageable pageable);
}
