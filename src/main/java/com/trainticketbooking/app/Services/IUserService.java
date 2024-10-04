package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService extends IService<User> {
    public User findByUsername(String username);
    Page<User> findAll(Pageable pageable);
}
