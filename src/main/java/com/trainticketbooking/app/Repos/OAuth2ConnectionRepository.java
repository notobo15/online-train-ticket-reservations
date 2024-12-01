package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.OAuth2Connection;
import com.trainticketbooking.app.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OAuth2ConnectionRepository extends JpaRepository<OAuth2Connection, Long> {
    Optional<OAuth2Connection> findByUserAndProvider(User user, String provider);
    boolean existsByUserAndProvider(User user, String provider);
}