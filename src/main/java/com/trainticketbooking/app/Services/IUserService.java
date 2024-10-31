package com.trainticketbooking.app.Services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.trainticketbooking.app.Entities.User;

public interface IUserService extends IService<User> {
    public User findByUsername(String username);
    public User findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    public User adminUpdateUser(User user);
    public String generateResetToken(User user);
}
