package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByPaypalOrderId(String paypalOrderId);
}
