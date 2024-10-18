package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.ResetToken;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Repos.ResetTokenRepository;
import com.trainticketbooking.app.Repos.UserRepository;
import com.trainticketbooking.app.Services.IUserService;

import com.trainticketbooking.app.Utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public Optional<User> getById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        return userOptional;
    }

    public User save(User user) {
        if (user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        } else if (user.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        } else if (user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        return userRepository.save(user);
    }

    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(User user) {
        Optional<User> existingUser = userRepository.findById(user.getUserId());

        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setFullName(user.getFullName());
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            return userRepository.save(updatedUser);
        } else {
            throw new RuntimeException("User not found with id: " + user.getUserId());
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User adminUpdateUser(User user) {
        User userTemp = getById(user.getUserId()).get();
        user.setPassword(userTemp.getPassword());
        return userRepository.save(user);
    }

    public String generateResetToken(User user) {
        String token = UUID.randomUUID().toString();

        ResetToken resetToken = new ResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(DateUtils.calExpiryDate(12 * 60));
        resetTokenRepository.save(resetToken);

        return token;
    }

    @Transactional
    public void updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public ResetToken findByResetToken(String token) {
        return resetTokenRepository.findByToken(token);

    }

    @Transactional
    public void deleteResetToken(String token) {
        resetTokenRepository.deleteByToken(token);
    }
}