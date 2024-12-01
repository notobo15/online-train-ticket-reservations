package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.UserDto;
import com.trainticketbooking.app.Entities.ResetToken;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Repos.ResetTokenRepository;
import com.trainticketbooking.app.Repos.UserRepository;
import com.trainticketbooking.app.Services.IUserService;
import com.trainticketbooking.app.Utils.DateUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    private MediaService mediaService;

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
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
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

    public UserDto getCurrentUserDto() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {

            UserDto dto = new UserDto();
            dto.setUsername(authentication.getName());
            dto.setEmail(findByUsername(authentication.getName()).getEmail());

            String role = authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .findFirst()
                    .orElse("ROLE_USER");
            dto.setRole(role);

            return dto;
        }

        return null;
    }

    public User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            // Lấy thông tin username từ SecurityContext
            String username = authentication.getName();

            // Tìm người dùng trong cơ sở dữ liệu dựa trên username
            User currentUser = findByUsername(username);

            return currentUser;
        }
        return null; // Trả về null nếu không có thông tin đăng nhập
    }

    @Transactional
    public void deleteResetToken(String token) {
        resetTokenRepository.deleteByToken(token);
    }

    public void updateProfileInfo(User user) {
        User currentUser = getCurrentUser();
        // Cập nhật thông tin người dùng
        currentUser.setEmail(user.getEmail());
        currentUser.setFullName(user.getFullName());
        currentUser.setAddress(user.getAddress());
        currentUser.setPhone(user.getPhone());
        userRepository.save(currentUser); // Lưu thay đổi vào cơ sở dữ liệu
    }

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    public void saveProfileImage(MultipartFile profileImage) {
        String newFileName = mediaService.saveMedia(profileImage);
        User currentUser = getCurrentUser();
        currentUser.setProfileImage(newFileName); // Lưu tên hình ảnh
        userRepository.save(currentUser);
    }

    // Cập nhật mật khẩu
    public void updatePassword(String password) {
        User currentUser = getCurrentUser();
        currentUser.setPassword(password); // Giả sử mật khẩu đã được mã hóa
        userRepository.save(currentUser);
    }
}