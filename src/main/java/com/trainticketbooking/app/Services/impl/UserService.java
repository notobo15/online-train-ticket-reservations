package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Repos.UserRepository;
import com.trainticketbooking.app.Services.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

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
        // Kiểm tra xem user có tồn tại trong cơ sở dữ liệu không
        Optional<User> existingUser = userRepository.findById(user.getUserId());

        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            // Cập nhật các thông tin cần thiết từ đối tượng `user` được truyền vào
            updatedUser.setFullName(user.getFullName());
            updatedUser.setUsername(user.getUsername());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());  // Nếu cần cập nhật mật khẩu
            // Thực hiện lưu lại bản ghi đã cập nhật
            return userRepository.save(updatedUser);
        } else {
            throw new RuntimeException("User not found with id: " + user.getUserId());
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
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


}