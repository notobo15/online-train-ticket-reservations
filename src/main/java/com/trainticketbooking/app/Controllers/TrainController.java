package com.trainticketbooking.app.Controllers;

import java.util.Optional;
import java.util.StringJoiner;

import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Services.IRailwayNetworkService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Services.ITrainService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/admin/trains")
public class TrainController {

    @Autowired
    private ITrainService trainService;

    @Autowired
    private IRailwayNetworkService railwayNetworkService;

    @GetMapping({"", "/index"})
    public String getAllTrains(Model model, Pageable pageable) {
        Page<Train> trainPage = trainService.findAll(pageable);

        model.addAttribute("trains", trainPage.getContent());
        model.addAttribute("currentPage", trainPage.getNumber());
        model.addAttribute("totalPages", trainPage.getTotalPages());
        model.addAttribute("size", trainPage.getSize());

        return "admin/trains/index";
    }

    @GetMapping("/create")
    public String createTrain(Model model) {
        model.addAttribute("train", new Train());
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
        return "admin/trains/create";
    }

    @PostMapping("/create")
    public String saveCreateTrain(Model model,
                                  @Valid @ModelAttribute Train train,
                                  BindingResult result) {
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "Train created fail! => " + errorsJoiner);
            return "admin/trains/create";
        }
        try {
            Train trainResponse = trainService.save(train);
            model.addAttribute("train", new Train());
            model.addAttribute(
                    "successMessage",
                    "Train created successfully with id = " + trainResponse.getTrainId());
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Train created fail!  " + e.getMessage());
        }
        return "admin/trains/create";
    }

    @GetMapping("/edit/{id}")
    public String editTrain(@PathVariable("id") Integer id, Model model) {
        try {
            Optional<Train> trainOptional = trainService.getById(id);
            if (trainOptional.isPresent()) {
                Train train = trainOptional.get();
                model.addAttribute("train", train);
                model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
            } else {
                model.addAttribute(
                        "errorMessage",
                        String.format("Train has id = %d does not exist", id)
                );
            }
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Train edited fail!  " + e.getMessage());
        }
        return "admin/trains/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditTrain(@PathVariable("id") Integer id,
                                Model model,
                                @Valid @ModelAttribute Train train,
                                BindingResult result) {
        train.setTrainId(id);
        model.addAttribute("railwayNetworks", railwayNetworkService.getAll());
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "Train edit fail! => " + errorsJoiner);
            return "admin/trains/edit";
        }
        try {
            Train trainResponse = trainService.save(train);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited train successfully with id = %d",
                            trainResponse.getTrainId())
            );
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Edited train fail!  " +
                            e.getMessage());
        }
        return "admin/trains/edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteTrain(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            trainService.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("Delete train success with id = %d", id)
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete train fail!  " + e.getMessage());
        }
        return "redirect:/admin/trains/index";
    }
}
