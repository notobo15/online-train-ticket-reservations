package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Exceptions.CarriageClassNotFoundException;
import com.trainticketbooking.app.Exceptions.SeatTypeNotFoundException;
import com.trainticketbooking.app.Services.ICarriageClassService;
import com.trainticketbooking.app.Services.ICarriageService;
import com.trainticketbooking.app.Services.ISeatTypeService;
import com.trainticketbooking.app.Services.ITrainService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
@RequestMapping("/admin/carriages")
public class CarriageAdminController {
    @Autowired
    private ICarriageService mCarriageService;

    @Autowired
    private ICarriageClassService mCarriageClassService;

    @Autowired
    private ISeatTypeService mSeatTypeService;

    @Autowired
    private ITrainService mTrainService;


    @GetMapping({"", "/index"})
    public String index(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        HttpSession session) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Carriage> carriagePage = mCarriageService.findAll(pageRequest);

        model.addAttribute("carriages", carriagePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", carriagePage.getTotalPages());
        model.addAttribute("size", size);

        String currentUrl = String.format("/admin/carriages/index?page=%d&size=%d", page, size);
        session.setAttribute("previousCarriagesPage", currentUrl);

        return "admin/carriages/index";
    }

    @GetMapping("edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model, HttpSession session) {
        Optional<Carriage> carOpt = mCarriageService.getById(id);
        int ticketsOfCarriage = mCarriageService.getTicketNumberOfCarriage(id);
        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        if (carOpt.isPresent()) {
            Carriage car = carOpt.get();
            model.addAttribute("car", car);
            model.addAttribute("errorMap", errorMap);
            model.addAttribute("successMessages", successMessages);
            model.addAttribute("ticketsOfCarriage", ticketsOfCarriage);
            model.addAttribute("csms",car.getCarriageSeatMappings());
            return "admin/carriages/edit";
        }
        return (String) session.getAttribute("previousCarriagesPage");
    }

    @GetMapping("/{id}/tickets")
    public String getTickets(@PathVariable("id") Integer id, Model model) {
        List<Ticket> tickets = mCarriageService.getTicketsOfCarriage(id);
        Optional<Carriage> carOpt = mCarriageService.getById(id);

        if (carOpt.isPresent()) {
            model.addAttribute("tickets", tickets);
            model.addAttribute("car", carOpt.get());
            return "admin/carriages/tickets-of-carriage";
        }

        return "redirect:/admin/carriages/edit/" + id;
    }

    @GetMapping("/{id}/seats")
    public String getSeats(@PathVariable("id") Integer id, Model model) {
        List<CarriageSeatMapping> csms = mCarriageService.getCSMsByCarriageId(id);
        List<Seat> seats = mCarriageService.getSeatsOfCarriage(csms);
        Optional<Carriage> carOpt = mCarriageService.getById(id);

        if (carOpt.isPresent()) {
            model.addAttribute("csms", csms);
            model.addAttribute("seats", seats);
            model.addAttribute("car", carOpt.get());
            return "admin/carriages/seats-of-carriage";
        }

        return "redirect:/admin/carriages/edit/" + id;
    }

    @GetMapping("/{carriage-id}/seat/{seat-id}")
    public String getSeatDetail(@PathVariable("carriage-id") Integer carriageId,
                                @PathVariable("seat-id") Integer seatId,
                                Model model,
                                RedirectAttributes redirectAttributes) {

        try {
            CarriageSeatMapping csm = mCarriageService.getCSMByCarriageIdAndSeatId(carriageId,seatId);

            Carriage car = csm.getCarriage();
            Seat seat = csm.getSeat();
            List<Ticket> tickets = csm.getTickets();

            model.addAttribute("statusOfSeatInCarriage", csm.isStatus());
            model.addAttribute("car",car);
            model.addAttribute("seat",seat);
            model.addAttribute("tickets",tickets);
            return "admin/carriages/seat-detail";
        }
        catch (RuntimeException ex) {

            redirectAttributes.addFlashAttribute("errorMessages",
                    List.of(ex.getMessage()) );
            return "redirect:/admin/carriages/" + carriageId
                    +"/seats";
        }

    }
    
    @ModelAttribute("getAllCarriageClasses")
    public List<CarriageClass> getAllCarriageClasses() {
        return mCarriageClassService.getAll();
    }
    
    @ModelAttribute("getSeatTypes")
    public List<SeatType> getAllSeatTypes() {
        return mSeatTypeService.getAll();
    }

    @ModelAttribute("getAllTrains")
    public List<Train> getAllTrains() {
        return mTrainService.getAll();
    }


    @PostMapping("/edit/{id}")
    public String update(@PathVariable("id") Integer id, @Valid @ModelAttribute("car") Carriage car, BindingResult result, Model model) {
        car.setCarriageId(id);
        int ticketsOfCarriage = mCarriageService.getTicketNumberOfCarriage(id);
        Map<String, String> errorMap = new HashMap<>();
        List<String> successMessages = new ArrayList<>();
        model.addAttribute("errorMap", errorMap);
        model.addAttribute("successMessages", successMessages);
        model.addAttribute("csms", mCarriageService.getCSMsByCarriageId(id));
        model.addAttribute("ticketsOfCarriage", ticketsOfCarriage);

        if (result.hasErrors()) {
            return "admin/carriages/edit";
        }

        try {
            car = mCarriageService.update(car);
        }
        catch (CarriageClassNotFoundException ex) {
            errorMap.put("carriageClass", ex.getMessage());
            return "admin/carriages/edit";
        }
        catch (RuntimeException ex) {
            errorMap.put("generalError", ex.getMessage());
            return "admin/carriages/edit";
        }

        model.addAttribute("car", car);
        successMessages.add("Successfully Updated");
        return "admin/carriages/edit";
    }

//    @GetMapping("create")
//    public String create(Model model) {
//        CarriageClass cac = new CarriageClass();
//        Map<String, String> errorMap = new HashMap<>();
//
//        model.addAttribute("cac", cac);
//        model.addAttribute("errorMap", errorMap);
//        return "admin/carriage-classes/create";
//    }
//
//    @PostMapping("/store")
//    public String store(@Valid @ModelAttribute("cac") CarriageClass cac, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
//        Map<String, String> errorMap = new HashMap<>();
//        model.addAttribute("errorMap", errorMap);
//
//        if (result.hasErrors()) {
//            return "admin/carriage-classes/create";
//        }
//
//        try {
//            mCarriageClassService.save(cac);
//        } catch (RuntimeException ex) {
//            errorMap.put("generalError", ex.getMessage());
//            return "admin/carriage-classes/create";
//        }
//
//        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Created"));
//        return "redirect:/admin/carriage-classes/index";
//    }
//

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        mCarriageService.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessages", List.of("Successfully Deleted"));
        return "redirect:/admin/carriages/index";
    }
}
