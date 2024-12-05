package com.trainticketbooking.app.Controllers;


import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import com.trainticketbooking.app.Entities.Payment;
import com.trainticketbooking.app.Entities.Ticket;
import com.trainticketbooking.app.Services.IPaymentService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/payments")
public class PaymentAdminController {


    @Autowired
    private IPaymentService mPaymentService;


    @GetMapping({"","/", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Payment> paymentPage = mPaymentService.findAll(pageRequest);

        model.addAttribute("payments", paymentPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paymentPage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/payments/index?page=%d&size=%d", page, size);
        session.setAttribute("previousPaymentsPage", currentUrl);

        return "admin/payments/index";
    }


    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Payment> paymentOpt = mPaymentService.getById(id);
        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            model.addAttribute("payment", payment);
            return "admin/payments/edit";
        }
        return (String) session.getAttribute("previousPaymentsPage");
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("payment") Payment payment, Model model,
                         RedirectAttributes redirectAttributes) {
        payment.setPaymentId(id);

        List<String> errorMessages = new ArrayList<>();
        List<String> successMessages = new ArrayList<>();
        redirectAttributes.addFlashAttribute("errorMessages",errorMessages);
        redirectAttributes.addFlashAttribute("successMessages",successMessages);

        try {
            payment =  mPaymentService.update(payment);
        }
        catch (RuntimeException ex) {
            errorMessages.add(ex.getMessage());
            return "redirect:/" +  "admin/payments/edit/" + id;
        }

        successMessages.add("Successfully Updated");
        return "redirect:/" +  "admin/payments/edit/" + id;
    }
}
