package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService extends IService<User> {
    public User findByUsername(String username);
}
