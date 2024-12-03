package com.trainticketbooking.app.Controllers;

import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Entities.TrainJourney;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.ITrainService;
import com.trainticketbooking.app.Services.impl.TrainJourneyService;
import com.trainticketbooking.app.Services.impl.TrainService;
import com.trainticketbooking.app.Services.impl.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/admin/train-journeys")
public class TrainJourneyController {

    @Autowired
    private TrainJourneyService trainJourneyService;

    @Autowired
    private ITrainService trainService;

    @Autowired
    private UserService userService;

    @GetMapping({"", "/index"})
    public String getAllTrainJourneys(Model model) {
        // Giả sử groupedJourneys chứa các ngày và số lượng chuyến tàu
        Map<LocalDate, Long> groupedJourneys = trainJourneyService.getJourneysGroupedByDate();

        // Tạo một map mới chứa danh sách tên tàu cho từng ngày
        Map<LocalDate, String> groupedTrainNames = new HashMap<>();
        for (Map.Entry<LocalDate, Long> entry : groupedJourneys.entrySet()) {
            List<TrainJourney> journeysForDate = trainJourneyService.getJourneysByDate(entry.getKey());
            String trainNames = journeysForDate.stream()
                    .map(journey -> journey.getTrain().getTrainNumber())
                    .collect(Collectors.joining(", ")); // Nối các tên tàu bằng dấu phẩy
            groupedTrainNames.put(entry.getKey(), trainNames);
        }

        model.addAttribute("groupedJourneys", groupedJourneys);
        model.addAttribute("groupedTrainNames", groupedTrainNames);

        return "admin/train-journeys/index";
    }

//    @GetMapping("/create")
//    public String createTrainJourney(Model model) {
//        model.addAttribute("trainJourney", new TrainJourney());
//
//        model.addAttribute("trains", trainService.getAll());  // Provide all trains for selection
//
//        User currentUser = userService.getCurrentUser();
//        if (currentUser != null) {
//            model.addAttribute("user", currentUser);
//        }
//        return "admin/train-journeys/create";  // This view will allow creating new journeys
//    }

    @GetMapping("/create")
    public String createTrainJourney(@RequestParam(value = "departureDate", required = false)
                                     @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departureDate, Model model) {
        // Nếu departureDate là null, gán giá trị mặc định là ngày hiện tại
        if (departureDate == null) {
            departureDate = LocalDate.now();
        }

        // Lấy tất cả tàu
        List<Train> trains = trainService.getAll();

        // Lấy các hành trình cho ngày đã chọn
        List<TrainJourney> journeysForDate = trainJourneyService.getTrainJourneysByDate(departureDate);

        // Lấy danh sách trainId của các chuyến tàu đã có hành trình
        List<Integer> journeyTrainIds = journeysForDate.stream()
                .map(trainJourney -> trainJourney.getTrain().getTrainId())
                .collect(Collectors.toList());

        model.addAttribute("trains", trains);
        model.addAttribute("journeyTrainIds", journeyTrainIds);  // Danh sách ID của các chuyến tàu đã có hành trình
        model.addAttribute("departureDate", departureDate);

        User currentUser = userService.getCurrentUser();
        if (currentUser != null) {
            model.addAttribute("user", currentUser);
        }

        return "admin/train-journeys/create";
    }


    @PostMapping("/create")
    public String updateTrainJourneys(@RequestParam("selectedTrains") List<Integer> selectedTrains,
                                      @RequestParam("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departureDate) {
        // Xử lý cập nhật hành trình cho các tàu đã chọn vào ngày đã chọn
        // Xóa các chuyến tàu cũ trước (nếu cần)
        trainJourneyService.deleteByDate(departureDate);

        // Thêm các chuyến tàu mới
        for (Integer trainId : selectedTrains) {
            Optional<Train> trainOptional = trainService.getById(trainId);
            if (trainOptional.isPresent()) {
                TrainJourney journey = new TrainJourney();
                journey.setTrain(trainOptional.get());
                journey.setDepartureDate(departureDate);
                trainJourneyService.save(journey);
            }
        }

        // Quay lại trang với thông báo thành công
        return "redirect:/admin/train-journeys/create?departureDate=" + departureDate;
    }


    @GetMapping("/create-range")
    public String createTrainJourney( Model model) {

        model.addAttribute("trains", trainService.getAll());
        return "admin/train-journeys/create-range";
    }
    @PostMapping("/create-range")
    public String createTrainJourney(@RequestParam("selectedTrains") List<Integer> selectedTrains,
                                     @RequestParam("departureDate") String departureDate) {

        if (selectedTrains == null || selectedTrains.isEmpty()) {
            // Trả về lỗi nếu không có tàu nào được chọn
            return "error"; // hoặc một trang thông báo lỗi
        }

        // Định dạng ngày theo "YYYY-MM-DD" để chuyển đổi chuỗi thành LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Tách departureDate thành 2 phần: startDate và endDate
        String[] dateParts = departureDate.split(" - "); // Giả sử định dạng là "YYYY-MM-DD - YYYY-MM-DD"
        String startDateString = dateParts[0]; // Ngày bắt đầu
        String endDateString = dateParts[1]; // Ngày kết thúc

        // Chuyển đổi từ String thành LocalDate
        LocalDate startDate = LocalDate.parse(startDateString, formatter); // Ngày bắt đầu
        LocalDate endDate = LocalDate.parse(endDateString, formatter); // Ngày kết thúc

        // Tiến hành thêm hành trình cho các tàu đã chọn trong phạm vi ngày
        for (Integer trainId : selectedTrains) {
            TrainJourney journey = new TrainJourney();
            Optional<Train> trainOptional = trainService.getById(trainId);
            if (trainOptional.isPresent()) {
                journey.setTrain(trainOptional.get());
                journey.setDepartureDate(startDate);  // Gán ngày khởi hành là ngày bắt đầu của phạm vi

                // Lưu journey vào cơ sở dữ liệu (ví dụ với JPA hoặc JDBC)
                trainJourneyService.save(journey);
            } else {
                return "redirect:/admin/train-journeys"; // Nếu không tìm thấy tàu
            }
        }

        return "redirect:/admin/train-journeys"; // Trở lại trang danh sách hành trình
    }


//    @PostMapping("/create")
//    public String createTrainJourney(@RequestParam("selectedTrains") List<Integer> selectedTrains,
//                                     @RequestParam("departureDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate departureDate) {
//        if (selectedTrains == null || selectedTrains.isEmpty()) {
//            // Nếu không có tàu nào được chọn, trả về lỗi hoặc thông báo
//            return "error"; // Hoặc trả về một trang lỗi cho người dùng
//        }
//
//        // Tiến hành thêm hành trình cho các tàu đã chọn
//        for (Integer trainId : selectedTrains) {
//            Optional<Train> trainOptional = trainService.getById(trainId);
//            if (trainOptional.isPresent()) {
//                TrainJourney journey = new TrainJourney();
//                journey.setTrain(trainOptional.get());
//                journey.setDepartureDate(departureDate);
//
//                // Lưu journey vào cơ sở dữ liệu
//                try {
//                    trainJourneyService.save(journey);
//                } catch (Exception e) {
//                    // Xử lý lỗi khi lưu (nếu có)
//                    return "redirect:/admin/train-journeys?error=true"; // Hoặc trả về trang lỗi
//                }
//            } else {
//                // Nếu không tìm thấy tàu, có thể trả về thông báo lỗi
//                return "redirect:/admin/train-journeys?trainNotFound=true";
//            }
//        }
//
//        // Quay lại trang danh sách hành trình sau khi thành công
//        return "redirect:/admin/train-journeys";
//    }

    @PostMapping("/delete/{id}")
    public String deleteTrainJourney(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            trainJourneyService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Train journey deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to delete train journey: " + e.getMessage());
        }
        return "redirect:/admin/train-journeys/index";
    }

    @PostMapping("/train-journey/create")
    public String saveTrainJourney(@RequestParam("selectedTrains") List<Integer> selectedTrainIds,
                                   @RequestParam("departureDate") String departureDateStr,
                                   RedirectAttributes redirectAttributes) {
        try {
            LocalDate departureDate = LocalDate.parse(departureDateStr);

            for (Integer trainId : selectedTrainIds) {
                Train train = trainService.getById(trainId).orElseThrow(() -> new RuntimeException("Train not found"));
                TrainJourney trainJourney = new TrainJourney();
                trainJourney.setTrain(train);
                trainJourney.setDepartureDate(departureDate);
                trainJourney.setStatus("Upcoming");  // Mặc định trạng thái là 'Upcoming'
                trainJourneyService.save(trainJourney);
            }

            redirectAttributes.addFlashAttribute("successMessage", "Train journeys created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create train journey: " + e.getMessage());
        }
        return "redirect:/admin/train-journeys/index";
    }
}
