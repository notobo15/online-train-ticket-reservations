package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.RailwayNetwork;
import com.trainticketbooking.app.Services.IRailwayNetworkService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
    public String saveCreateRailwayNetwork(Model model,
                                           @Valid @ModelAttribute RailwayNetwork railwayNetwork,
                                           BindingResult result) {
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            log.error("Railway Network created fail: {}", errorsJoiner);
            model.addAttribute(
                    "errorMessage",
                    "Railway Network created fail! => " + errorsJoiner);
            return "admin/railway-network/create";
        }
        try {
            RailwayNetwork railwayNetworkResponse = railwayNetworkService.save(railwayNetwork);
            model.addAttribute("railwayNetwork", new RailwayNetwork());
            model.addAttribute(
                    "successMessage",
                    "Railway Network created successfully with id = " +
                            railwayNetworkResponse.getRailwayId());
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
            if (railwayNetworkOptional.isPresent()) {
                RailwayNetwork railwayNetwork = railwayNetworkOptional.get();
                model.addAttribute("railwayNetwork", railwayNetwork);
            } else {
                model.addAttribute(
                        "errorMessage",
                        String.format("railway network id = %d does not exist", id)
                );
            }
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "railway-network edited fail!  " + e.getMessage());
        }
        return "admin/railway-network/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditRailwayNetwork(@PathVariable("id") Integer id,
                                         Model model,
                                         @Valid @ModelAttribute RailwayNetwork railwayNetwork,
                                         BindingResult result) {
        railwayNetwork.setRailwayId(id);
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(" / "));
            log.error("Railway Network edited fail: {}", errors);
            model.addAttribute("errorMessage",
                    "Railway Network edited fail! => " + errors);
            return "admin/railway-network/edit";
        }
        try {
            log.info("RailwayNetwork update: {}", railwayNetwork.toString());
            RailwayNetwork railwayNetworkResponse = railwayNetworkService.save(railwayNetwork);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited railwayNetwork successfully with id = %d", railwayNetworkResponse.getRailwayId())
            );
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
            if (railwayNetworkOptional.isPresent()) {
                RailwayNetwork railwayNetwork = railwayNetworkOptional.get();
                model.addAttribute("railwayNetwork", railwayNetwork);
            } else {
                model.addAttribute(
                        "errorMessage",
                        String.format("railway network id = %d does not exist", id)
                );
            }
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
