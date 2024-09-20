package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> getAllUsers();
    public Optional<User> getUserById(Integer id);
    public User saveUser(User user);
    public void deleteUserById(Integer id);
    public User findByUsername(String username);
}
