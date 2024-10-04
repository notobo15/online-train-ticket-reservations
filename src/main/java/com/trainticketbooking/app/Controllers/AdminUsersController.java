package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.ITrainService;
import com.trainticketbooking.app.Services.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUsersController {
    IUserService userService;

    @GetMapping({"", "/index"})
    public String getAllUsers(Model model, Pageable pageable) {

        Page<User> userPage = userService.findAll(pageable);

        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", userPage.getNumber());
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("size", userPage.getSize());

        return "admin/users/index";
    }

    @GetMapping("/create")
    public String createUser(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/create";
    }

    @PostMapping("/create")
    public String saveUser(Model model, @ModelAttribute User user) {
        try {
            User userResponse = userService.save(user);
            model.addAttribute("user", new User());
            model.addAttribute(
                    "successMessage",
                    "User created successfully!");
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "User created fail!  " + e.getMessage());
        } finally {
            return "admin/users/create";
        }
    }

//    @GetMapping("detail/{id}")
//    public String getDetails(@PathVariable("id") Integer id, Model model) {
//        Optional<Train> train = trainService.getById(id);
//        if (train.isPresent()) {
//            model.addAttribute("train", train.get());
//            return "admin/trains/detail";
//        }
//        return "admin/trains/index";
//    }

}
