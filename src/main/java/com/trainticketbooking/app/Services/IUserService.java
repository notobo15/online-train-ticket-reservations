package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Dtos.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.trainticketbooking.app.Entities.User;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService extends IService<User> {
    public User findByUsername(String username);
    public User findByEmail(String email);
    Page<User> findAll(Pageable pageable);
    public User adminUpdateUser(User user);
    public String generateResetToken(User user);
    public UserDto getCurrentUserDto();
    public User getCurrentUser();
    public void updateProfileInfo(User user);

    public void saveProfileImage(MultipartFile profileImage);

    public void updatePassword(String password);
}
