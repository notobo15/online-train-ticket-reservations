package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.User;

public interface IUserService extends IService<User> {
    public User findByUsername(String username);
}
