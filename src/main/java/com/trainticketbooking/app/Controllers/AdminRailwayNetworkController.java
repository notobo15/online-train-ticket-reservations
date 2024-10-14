package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Services.IRailwayNetworkService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

@Controller
@RequestMapping("/admin/railway-network")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminRailwayNetworkController {
    IRailwayNetworkService railwayNetworkService;

    @GetMapping({"", "/index"})
    public String getAllRailwayNetworks(Model model, Pageable pageable) {

        Page<RailwayNetwork> railwayNetworkPage = railwayNetworkService.findAll(pageable);

        model.addAttribute("railwayNetworks", railwayNetworkPage.getContent());
        model.addAttribute("currentPage", railwayNetworkPage.getNumber());
        model.addAttribute("totalPages", railwayNetworkPage.getTotalPages());
        model.addAttribute("size", railwayNetworkPage.getSize());

        return "admin/railway-network/index";
    }

    @GetMapping("/create")
    public String createRailwayNetwork(Model model) {
        model.addAttribute("railwayNetwork", new RailwayNetwork());
        return "admin/railway-network/create";
    }

    @PostMapping("/create")
    public String saveCreateRailwayNetwork(Model model, @ModelAttribute RailwayNetwork railwayNetwork) {
        try {
            RailwayNetwork railwayNetworkResponse = railwayNetworkService.save(railwayNetwork);
            model.addAttribute("railwayNetwork", new RailwayNetwork());
            model.addAttribute(
                    "successMessage",
                    "Railway Network created successfully with id = " + railwayNetworkResponse.getRailwayId());
        } catch (ConstraintViolationException ex) {
            // Lấy các lỗi validation
            List<String> errors = ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)  // Lấy thông báo lỗi từ từng violation
                    .toList();
            StringJoiner stringJoiner = new StringJoiner(" / ");
            errors.forEach(stringJoiner::add);
            model.addAttribute(
                    "errorMessage",
                    "Railway Network created fail! / " + stringJoiner.toString());
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Railway Network created fail!  " + e.getMessage());
        }
        return "admin/railway-network/create";
    }

    @GetMapping("/edit/{id}")
    public String editRailwayNetwork(@PathVariable("id") Integer id, Model model) {
        log.info("Start edit railway-network");
        try {
            Optional<RailwayNetwork> railwayNetworkOptional = railwayNetworkService.getById(id);
            RailwayNetwork railwayNetwork = railwayNetworkOptional.get();
            model.addAttribute("railwayNetwork", railwayNetwork);
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "railway-network edited fail!  " + e.getMessage());
        }
        return "admin/railway-network/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditRailwayNetwork(@PathVariable("id") Integer id,
                                         @ModelAttribute RailwayNetwork railwayNetwork,
                                         Model model) {
        try {
            railwayNetwork.setRailwayId(id);
            log.info("RailwayNetwork update: {}", railwayNetwork.toString());
            RailwayNetwork railwayNetworkResponse = railwayNetworkService.save(railwayNetwork);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited railwayNetwork successfully with id = %d", railwayNetworkResponse.getRailwayId())
            );
        } catch (ConstraintViolationException ex) {
            // Lấy các lỗi validation
            List<String> errors = ex.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)  // Lấy thông báo lỗi từ từng violation
                    .toList();
            StringJoiner stringJoiner = new StringJoiner(" / ");
            errors.forEach(stringJoiner::add);
            model.addAttribute(
                    "errorMessage",
                    "Railway Network created fail! / " + stringJoiner.toString());
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Railway Network created fail!  " + e.getMessage());
        }
        return "admin/railway-network/edit";
    }

    @GetMapping("detail/{id}")
    public String viewDetailRailwayNetwork(@PathVariable("id") Integer id, Model model) {
        try {
            Optional<RailwayNetwork> railwayNetworkOptional = railwayNetworkService.getById(id);
            RailwayNetwork railwayNetwork = railwayNetworkOptional.get();
            model.addAttribute("railwayNetwork", railwayNetwork);
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "View detail railwayNetwork fail!  " + e.getMessage());
        }
        return "admin/railway-network/detail";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            railwayNetworkService.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("Delete railwayNetwork success with id = %d", id)
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete railwayNetwork fail!  " + e.getMessage());
        }
        return "redirect:/admin/railway-network/index";
    }
}
