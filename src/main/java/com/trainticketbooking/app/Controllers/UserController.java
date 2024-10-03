package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.impl.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("users", users);
        return "users/list"; // Đường dẫn đến file view hiển thị danh sách người dùng
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Integer id, Model model) {
        Optional<User> user = userService.getById(id);
        model.addAttribute("user", user.orElse(null));
        return "users/view"; // Đường dẫn đến file view hiển thị chi tiết người dùng
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "users/create"; // Đường dẫn đến file view tạo người dùng mới
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/update/{id}")
    public String showEditForm(@PathVariable("id") Integer id, Model model) {
        Optional<User> user = userService.getById(id);
        model.addAttribute("user", user.orElse(null));
        return "users/update"; // Đường dẫn đến file view chỉnh sửa người dùng
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @ModelAttribute("user") User user) {
        user.setUserId(id); // Đảm bảo cập nhật đúng người dùng
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return "redirect:/users";
    }
}