package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Services.ITrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/trains")
public class TrainController {

    @Autowired
    private ITrainService trainService;

    @GetMapping
    public String getAllTrains(Model model) {
        model.addAttribute("trains", trainService.getAll());
        return "admin/trains/index";
    }

    @GetMapping("/create")
    public String showAddTrainForm(Model model) {
        model.addAttribute("train", new Train());
        return "admin/trains/create";
    }

    // Xử lý việc thêm train mới
    @PostMapping("/create")
    public String addTrain(@ModelAttribute Train train) {
        trainService.save(train);
        return "redirect:/trains"; // Chuyển hướng về danh sách trains sau khi thêm thành công
    }

    // Hiển thị form để chỉnh sửa train
    @GetMapping("/edit/{id}")
    public String showEditTrainForm(@PathVariable("id") Integer id, Model model) {
        Optional<Train> train = trainService.getById(id);
        if (train.isPresent()) {
            model.addAttribute("train", train.get());
            return "admin/trains/edit";
        } else {
            return "redirect:/trains";
        }
    }

    @PostMapping("/edit/{id}")
    public String updateTrain(@PathVariable("id") Integer id, @ModelAttribute Train train) {
        train.setTrainId(id);
        trainService.update(train);
        return "redirect:/trains";
    }

    // Xóa train theo ID
    @GetMapping("/delete/{id}")
    public String deleteTrain(@PathVariable("id") Integer id) {
        trainService.deleteById(id);
        return "redirect:/trains";
    }
}
