package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Role;
import com.trainticketbooking.app.Services.IRoleService;
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

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminRolesController {
    IRoleService roleService;

    @GetMapping({"", "/index"})
    public String getAllRoles(Model model, Pageable pageable) {

        Page<Role> rolePage = roleService.findAll(pageable);

        model.addAttribute("roles", rolePage.getContent());
        model.addAttribute("currentPage", rolePage.getNumber());
        model.addAttribute("totalPages", rolePage.getTotalPages());
        model.addAttribute("size", rolePage.getSize());

        return "admin/roles/index";
    }

    @GetMapping("/create")
    public String createRole(Model model) {
        model.addAttribute("role", new Role());
        return "admin/roles/create";
    }

    @PostMapping("/create")
    public String saveCreateRole(Model model,
                                 @Valid @ModelAttribute Role role,
                                 BindingResult result) {
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            log.error("Role created fail! => {}", errorsJoiner);
            model.addAttribute(
                    "errorMessage",
                    "Role created fail! => " + errorsJoiner);
            return "admin/roles/create";
        }
        try {
            Role roleResponse = roleService.save(role);
            model.addAttribute("role", new Role());
            model.addAttribute(
                    "successMessage",
                    "Role created successfully with id = " + roleResponse.getRoleId());
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Role created fail!  " + e.getMessage());
        }
        return "admin/roles/create";
    }

    @GetMapping("/edit/{id}")
    public String editRole(@PathVariable("id") Integer id, Model model) {
        log.info("Start edit role");
        try {
            Optional<Role> roleOptional = roleService.getById(id);
            if (roleOptional.isPresent()) {
                Role role = roleOptional.get();
                model.addAttribute("role", role);
            } else {
                model.addAttribute(
                        "errorMessage",
                        String.format("Role id = %d does not exist", id)
                );
            }
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Role edited fail!  " + e.getMessage());
        }
        return "admin/roles/edit";
    }

    @PostMapping("/edit/{id}")
    public String saveEditRole(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute Role role,
                               BindingResult result,
                               Model model) {
        role.setRoleId(id);
        if (result.hasErrors()) {
            StringJoiner errorsJoiner = new StringJoiner(" / ");
            result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage) // Lấy message của từng lỗi
                    .forEach(errorsJoiner::add); // Thêm message vào StringJoiner
            model.addAttribute(
                    "errorMessage",
                    "Role edited fail! => " + errorsJoiner);
            return "admin/roles/edit";
        }
        try {
            roleService.save(role);
            model.addAttribute(
                    "successMessage",
                    String.format("Edited role with id = %d successfully!", role.getRoleId())
            );
        } catch (Exception e) {
            model.addAttribute(
                    "errorMessage",
                    "Role edited fail!  " + e.getMessage());
        }
        return "admin/roles/edit";
    }

    @PostMapping("/delete/{id}")
    public String deleteRole(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            roleService.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    String.format("Delete role success with id = %d", id)
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Delete role fail!  " + e.getMessage());
        }
        return "redirect:/admin/roles/index";
    }

}
