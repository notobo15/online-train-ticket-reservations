package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatHolds.SeatHoldResponseDto;
import com.trainticketbooking.app.Dtos.SeatType.SeatTypePriceDTO;
import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Mappers.SeatHoldMapper;
import com.trainticketbooking.app.Repos.*;
import com.trainticketbooking.app.Services.IStationService;
import com.trainticketbooking.app.Utils.CanBookUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SeatHoldService {

    @Autowired
    private SeatHoldRepository seatHoldRepository;

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private TrainJourneyRepository trainJourneyRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private TrainService trainService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatHoldMapper seatHoldMapper;

    @Autowired
    private CarriageService carriageService;

    @MessageMapping("/seatHold")
    @SendTo("/topic/seats")
    public SeatHoldResponseDto createSeatHold(CreateSeatHoldRequestDto seatHoldRequestDto) {


        seatHoldRequestDto.setArrivalStationId(stationRepository.findByCode(seatHoldRequestDto.getArrivalStationCode()).getStationId());
        seatHoldRequestDto.setDepartureStationId(stationRepository.findByCode(seatHoldRequestDto.getDepartureStationCode()).getStationId());

        // 1. Find the train
        Optional<Train> trainOptional = trainRepository.findById(seatHoldRequestDto.getTrainId());
        if (trainOptional.isEmpty()) {
            throw new RuntimeException("Train not found with ID: " + seatHoldRequestDto.getTrainId());
        }

        Train train = trainOptional.get();

        // 2. Find the routes for the given train
        List<Route> routes = routeRepository.findByTrain(train);

        // 3. Find the journey for the specific date
        LocalDate journeyDate = seatHoldRequestDto.getDepartureDate();
        TrainJourney trainJourney = trainJourneyRepository.findByTrainAndDepartureDate(train, journeyDate);
        if (trainJourney == null) {
            throw new RuntimeException("No train journey found for the train ID: " + seatHoldRequestDto.getTrainId() +
                    " on the date: " + journeyDate);
        }

        Optional<Seat> seatOptional = seatRepository.findById(seatHoldRequestDto.getSeatId());
        if (seatOptional.isEmpty()) {
            throw new RuntimeException("Seat not found with ID: " + seatHoldRequestDto.getSeatId());
        }
        Seat seat = seatOptional.get();

//        List<SeatHold> existingSeatHolds = seatHoldRepository.findByTrainAndDepartureDateAndSeat(train, journeyDate, seat);
        List<SeatHold> existingSeatHolds = updateSeatHoldsWithExpiredRemoved(train, seatHoldRequestDto.getDepartureDate(), seat);

        var allStationIdsByTrain = trainService.getStationIdsByTrain(trainOptional.get());

        var allStationIds = trainService.getStationIdsBetweenDepartureAndArrival(seatHoldRequestDto.getDepartureStationId(), seatHoldRequestDto.getArrivalStationId(), train);

        List<Set<Integer>> allStationIdsFromSeatHolds = new ArrayList<>();
        for (SeatHold existingSeatHold : existingSeatHolds) {
            Set<Integer> stationIds = trainService.getStationIdsBetweenDepartureAndArrival(existingSeatHold.getDepartureStation().getStationId(), existingSeatHold.getArrivalStation().getStationId(), train);
            allStationIdsFromSeatHolds.add(stationIds);
        }


        if (CanBookUtil.canBook(allStationIds, allStationIdsFromSeatHolds, allStationIdsByTrain)) {

            log.info("co the book");

            SeatHold seatHold = new SeatHold();
            seatHold.setTrain(train);
            seatHold.setSeat(seat);
            seatHold.setDepartureDate(seatHoldRequestDto.getDepartureDate());
            seatHold.setStatus("holding");

            // Fetch the stations from the repository
            Optional<Station> departureStationOptional = stationRepository.findById(seatHoldRequestDto.getDepartureStationId());
            Optional<Station> arrivalStationOptional = stationRepository.findById(seatHoldRequestDto.getArrivalStationId());

            if (departureStationOptional.isEmpty() || arrivalStationOptional.isEmpty()) {
                throw new RuntimeException("Invalid station IDs provided.");
            }

            seatHold.setDepartureStation(departureStationOptional.get());
            seatHold.setArrivalStation(arrivalStationOptional.get());

            seatHold = seatHoldRepository.save(seatHold);

            var dto = seatHoldMapper.toDto(seatHold);


            // Xác định startRoute và endRoute
            Route startRoute = routes.stream()
                    .filter(r -> r.getStartStation().getCode().equals(seatHoldRequestDto.getDepartureStationCode()))
                    .findFirst().orElse(null);

            Route endRoute = routes.stream()
                    .filter(r -> r.getEndStation().getCode().equals(seatHoldRequestDto.getArrivalStationCode()))
                    .findFirst().orElse(null);

            List<Route> relevantRoutes = routes.stream()
                    .filter(r -> r.getStationNumber() >= startRoute.getStationNumber() &&
                            r.getStationNumber() <= endRoute.getStationNumber())
                    .sorted((r1, r2) -> Integer.compare(r1.getStationNumber(), r2.getStationNumber()))
                    .collect(Collectors.toList());


            double totalDistance = relevantRoutes.stream()
                    .mapToDouble(Route::getDistance)
                    .sum();

            var seatType = seat.getSeatType();

            dto.getSeat().setPrice(seatType.getPrice().calTotalPrice(totalDistance));
            dto.getSeat().setSeatType(seatType.getCode());
            // Trả về DTO sau khi lưu
            return dto;
        } else {
            log.info("khong the book");
            return null;
        }
    }

    /**
     * Deletes a SeatHold by its ID.
     *
     * @param id The ID of the SeatHold to delete
     */
    public void deleteSeatHoldById(Integer id) {
        // Check if SeatHold exists before deleting
        if (seatHoldRepository.existsById(id)) {
            seatHoldRepository.deleteById(id);
        } else {
            throw new RuntimeException("SeatHold not found with ID: " + id);
        }
    }  public void deleteSeatHold(CreateSeatHoldRequestDto seatHoldRequestDto) {
       var seat = seatHoldRepository.findBySeat_SeatIdAndDepartureDateAndTrain_TrainId(seatHoldRequestDto.getSeatId(), seatHoldRequestDto.getDepartureDate(), seatHoldRequestDto.getTrainId());

        if (seat.isPresent()) {
            seatHoldRepository.delete(seat.get());
        } else {
            throw new RuntimeException("SeatHold not delete");
        }
    }

    // Kiểm tra xem SeatHold đã hết hạn chưa
    public boolean isSeatHoldExpired(SeatHold seatHold) {
        return seatHold.getExpirationTime().isBefore(LocalDateTime.now());
    }

    // Xóa các SeatHold đã hết hạn
    public void deleteExpiredSeatHolds() {
        // Lấy tất cả SeatHold chưa hết hạn
        List<SeatHold> expiredSeatHolds = seatHoldRepository.findAll().stream()
                .filter(this::isSeatHoldExpired) // Chỉ giữ lại những SeatHold đã hết hạn
                .collect(Collectors.toList());

        // Xóa các SeatHold đã hết hạn
        for (SeatHold seatHold : expiredSeatHolds) {
            seatHoldRepository.delete(seatHold);
        }
    }

    public List<SeatHold> getValidSeatHolds(List<SeatHold> existingSeatHolds) {
        return existingSeatHolds.stream()
                .filter(seatHold -> !isSeatHoldExpired(seatHold))  // Chỉ giữ lại những SeatHold chưa hết hạn
                .collect(Collectors.toList());
    }


    // Tìm các SeatHold hợp lệ cho một chuyến tàu cụ thể và ngày đi cụ thể
    public List<SeatHold> findValidSeatHolds(Train train, LocalDate journeyDate, Seat seat) {
        List<SeatHold> existingSeatHolds = seatHoldRepository.findByTrainAndDepartureDateAndSeat(train, journeyDate, seat);

        // Lọc ra các SeatHold còn hiệu lực
        return getValidSeatHolds(existingSeatHolds);
    }

    // Tìm và xóa các SeatHold hết hạn khi cần thiết
    public void cleanUpExpiredSeatHolds() {
        deleteExpiredSeatHolds();  // Gọi phương thức để xóa các SeatHold đã hết hạn
    }

    // Phương thức này sẽ xóa các SeatHold đã hết hạn từ danh sách và cập nhật lại danh sách
    public List<SeatHold> updateSeatHoldsWithExpiredRemoved(Train train, LocalDate journeyDate, Seat seat) {
        // Lấy danh sách tất cả SeatHold cho chuyến tàu, ngày và ghế cụ thể
        List<SeatHold> existingSeatHolds = seatHoldRepository.findByTrainAndDepartureDateAndSeat(train, journeyDate, seat);

        // Lọc các SeatHold đã hết hạn
        List<SeatHold> expiredSeatHolds = existingSeatHolds.stream()
                .filter(this::isSeatHoldExpired)  // Chỉ giữ lại các SeatHold đã hết hạn
                .collect(Collectors.toList());

        // Xóa các SeatHold đã hết hạn từ cơ sở dữ liệu
        if (!expiredSeatHolds.isEmpty()) {
            seatHoldRepository.deleteAll(expiredSeatHolds);
        }

        // Cập nhật lại danh sách các SeatHold hợp lệ sau khi xóa
        existingSeatHolds.removeAll(expiredSeatHolds); // Loại bỏ các SeatHold đã hết hạn

        return existingSeatHolds;  // Trả về danh sách SeatHold đã được cập nhật
    }


    public List<SeatDTO> getListSeats(SeatHoldRequestDto seatHoldRequestDto) {
        // 1. Find the train
        Optional<Train> trainOptional = trainRepository.findById(seatHoldRequestDto.getTrainId());
        if (trainOptional.isEmpty()) {
            throw new RuntimeException("Train not found with ID: " + seatHoldRequestDto.getTrainId());
        }

        seatHoldRequestDto.setArrivalStationId(stationRepository.findByCode(seatHoldRequestDto.getArrivalStationCode()).getStationId());
        seatHoldRequestDto.setDepartureStationId(stationRepository.findByCode(seatHoldRequestDto.getDepartureStationCode()).getStationId());

        Train train = trainOptional.get();

        // 2. Find the routes for the given train
        List<Route> routes = routeRepository.findByTrain(train);

        // 3. Find the journey for the specific date
        LocalDate journeyDate = seatHoldRequestDto.getDepartureDate();
        TrainJourney trainJourney = trainJourneyRepository.findByTrainAndDepartureDate(train, journeyDate);
        if (trainJourney == null) {
            throw new RuntimeException("No train journey found for the train ID: " + seatHoldRequestDto.getTrainId() +
                    " on the date: " + journeyDate);
        }

        // 4. Find the carriage by carriageId
        Optional<Carriage> carriageOptional = carriageService.getById(seatHoldRequestDto.getCarriageId());
        if (carriageOptional.isEmpty()) {
            throw new RuntimeException("Carriage not found with ID: " + seatHoldRequestDto.getCarriageId());
        }
        Carriage carriage = carriageOptional.get();

        // 5. Get all seats in the carriage
        List<Seat> seats = carriage.getSeats();  // Lấy tất cả các ghế trong carriage

        // 6. Create a list of SeatDTOs
        List<SeatDTO> seatDTOList = new ArrayList<>();

        // Xác định startRoute và endRoute
        Route startRoute = routes.stream()
                .filter(r -> r.getStartStation().getCode().equals(seatHoldRequestDto.getDepartureStationCode()))
                .findFirst().orElse(null);

        Route endRoute = routes.stream()
                .filter(r -> r.getEndStation().getCode().equals(seatHoldRequestDto.getArrivalStationCode()))
                .findFirst().orElse(null);

        List<Route> relevantRoutes = routes.stream()
                .filter(r -> r.getStationNumber() >= startRoute.getStationNumber() &&
                        r.getStationNumber() <= endRoute.getStationNumber())
                .sorted((r1, r2) -> Integer.compare(r1.getStationNumber(), r2.getStationNumber()))
                .collect(Collectors.toList());

        double totalDistance = relevantRoutes.stream()
                .mapToDouble(Route::getDistance)
                .sum();
        // 7. Check the availability of each seat in this carriage
        for (Seat seat : seats) {
            // Create a new SeatDTO for this seat
            SeatDTO seatDTO = new SeatDTO();
            seatDTO.setSeatId(seat.getSeatId());
            seatDTO.setSeatNumber(seat.getSeatNumber());
            seatDTO.setSeatType(seat.getSeatType().getCode());
            // Check if the seat can be booked for the specified journey and stations
            List<SeatHold> existingSeatHolds = updateSeatHoldsWithExpiredRemoved(train, journeyDate, seat);

            var allStationIdsByTrain = trainService.getStationIdsByTrain(train);

            var allStationIds = trainService.getStationIdsBetweenDepartureAndArrival(seatHoldRequestDto.getDepartureStationId(), seatHoldRequestDto.getArrivalStationId(), train);

            List<Set<Integer>> allStationIdsFromSeatHolds = new ArrayList<>();
            for (SeatHold existingSeatHold : existingSeatHolds) {
                Set<Integer> stationIds = trainService.getStationIdsBetweenDepartureAndArrival(existingSeatHold.getDepartureStation().getStationId(), existingSeatHold.getArrivalStation().getStationId(), train);
                allStationIdsFromSeatHolds.add(stationIds);
            }

            // 8. Determine the status of the seat
            String status;
            if (CanBookUtil.canBook(allStationIds, allStationIdsFromSeatHolds, allStationIdsByTrain)) {
                status = "available"; // Ghế có thể đặt
            } else {
                status = "booked";
            }

            seatDTO.setStatus(status);

            seatDTO.setPrice(seat.getSeatType().getPrice().calTotalPrice(totalDistance));




            // Add the SeatDTO to the list
            seatDTOList.add(seatDTO);
        }

        return seatDTOList;
    }

}
