package com.trainticketbooking.app.Controllers;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Services.ITrainService;

@Slf4j
@Controller
@RequestMapping("/admin/trains")
public class TrainController {

    @Autowired
    private ITrainService trainService;

    @GetMapping({"", "/index"})
    public String getAllTrains(Model model,
                               Pageable pageable,
                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), size);
        Page<Train> trainPage = trainService.findAll(pageRequest);

        model.addAttribute("trains", trainPage.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", trainPage.getTotalPages());

        log.info("trains" + trainPage.getContent());
        log.info("trainPage.getTotalPages()" + trainPage.getTotalPages());
        log.info("trainPage.currentPage()" + pageable.getPageNumber());
        log.info("size" + size);
        model.addAttribute("size", size);
        return "admin/trains/index";
    }

    @GetMapping("detail/{id}")
    public String getDetails(@PathVariable("id") Integer id, Model model) {
        Optional<Train> train = trainService.getById(id);
        if (train.isPresent()) {
            model.addAttribute("train", train.get());
            return "admin/trains/detail";
        }
        return "admin/trains/index";
    }

    @GetMapping("/create")
    public String showAddTrainForm(Model model) {
        model.addAttribute("train", new Train());
        return "admin/trains/create";
    }

    @PostMapping("/create")
    public String addTrain(@ModelAttribute Train train) {
        trainService.save(train);
        return "redirect:/admin/trains";
    }

    @GetMapping("/edit/{id}")
    public String showEditTrainForm(@PathVariable("id") Integer id, Model model) {
        Optional<Train> train = trainService.getById(id);
        if (train.isPresent()) {
            model.addAttribute("train", train.get());
        } else {
            model.addAttribute("errorMessage", "Train not found.");
        }
        return "admin/trains/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateTrain(@PathVariable("id") Integer id, @ModelAttribute Train train) {
        train.setTrainId(id);
        trainService.update(train);
        return "redirect:/admin/trains";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrain(@PathVariable("id") Integer id) {
        trainService.deleteById(id);
        return "redirect:/admin/trains";
    }
}
