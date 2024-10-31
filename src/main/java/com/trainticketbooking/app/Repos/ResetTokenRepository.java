package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    ResetToken findByToken(String token);
    void deleteByToken(String token);
}