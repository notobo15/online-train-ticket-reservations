package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.SeatHolds.CreateSeatHoldRequestDto;
import com.trainticketbooking.app.Dtos.SeatType.SeatTypePriceDTO;
import com.trainticketbooking.app.Dtos.TrainJourney.TrainJourneySearchDTO;
import com.trainticketbooking.app.Entities.*;
import com.trainticketbooking.app.Repos.*;
import com.trainticketbooking.app.Requests.TrainSearchRequestDTO;
import com.trainticketbooking.app.Services.ITrainService;
import com.trainticketbooking.app.Utils.DurationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TrainService implements ITrainService {

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private TrainJourneyRepository trainJourneyRepository;
    @Autowired
    private RouteRepository routeRepository;
    @Autowired
    private SeatTypeRepository seatTypeRepository;

//    public List<Seat> findSeatsByTrainId(Integer trainId) {
//        return seatRepository.findSeatsByTrainId(trainId);
//    }

    public List<Carriage> findCarriagesByTrainId(Integer trainId) {
        return carriageRepository.findByTrainTrainId(trainId);
    }

    @Override
    public List<Train> getAll() {
        return trainRepository.findAll();
    }

    @Override
    public Optional<Train> getById(Integer id) {
        return trainRepository.findById(id);
    }

    @Override
    public Train save(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public void deleteById(Integer id) {
        trainRepository.deleteById(id);
    }

    @Override
    public Train update(Train train) {
        Optional<Train> existingTrain = trainRepository.findById(train.getTrainId());
        if (existingTrain.isPresent()) {
            Train updatedTrain = existingTrain.get();
            updatedTrain.setTrainNumber(train.getTrainNumber());
            updatedTrain.setTrainType(train.getTrainType());
            return trainRepository.save(updatedTrain);
        }
        throw new RuntimeException("Train not found with id: " + train.getTrainId());
    }

    @Override
    public Page<Train> findAll(Pageable pageable) {
        return trainRepository.findAll(pageable);
    }

    public List<TrainJourneySearchDTO> searchTrainJourneys(TrainSearchRequestDTO request) {
        List<TrainJourney> trainJourneys = trainJourneyRepository.findAvailableTrainJourneys(
                request.getDepartureDate(),
                request.getStartStationCode(),
                request.getEndStationCode());

        return trainJourneys.stream().map(trainJourney -> mapToTrainJourneySearchDTO(trainJourney, request)).collect(Collectors.toList());
    }

    private TrainJourneySearchDTO mapToTrainJourneySearchDTO(TrainJourney trainJourney, TrainSearchRequestDTO request) {
        TrainJourneySearchDTO dto = new TrainJourneySearchDTO();
        dto.setTrainId(trainJourney.getTrain().getTrainId());
        dto.setTrainNumber(trainJourney.getTrain().getTrainNumber());
        dto.setTrainType(trainJourney.getTrain().getTrainType());
        dto.setDepartureDate(trainJourney.getDepartureDate());

        List<Route> routes = trainJourney.getTrain().getRoutes();

        // Xác định startRoute và endRoute
        Route startRoute = routes.stream()
                .filter(r -> r.getStartStation().getCode().equals(request.getStartStationCode()))
                .findFirst().orElse(null);

        Route endRoute = routes.stream()
                .filter(r -> r.getEndStation().getCode().equals(request.getEndStationCode()))
                .findFirst().orElse(null);

        if (startRoute != null && endRoute != null) {
            dto.setDepartureTime(startRoute.getDepartureTime());
            dto.setStartStationName(startRoute.getStartStation().getStationName());
            dto.setStartProvinceName(startRoute.getStartStation().getProvince().getName());
            dto.setStartStationCode(startRoute.getStartStation().getCode());

            dto.setArrivalTime(endRoute.getArrivalTime());
            dto.setEndStationName(endRoute.getEndStation().getStationName());
            dto.setEndProvinceName(endRoute.getEndStation().getProvince().getName());
            dto.setEndStationCode(endRoute.getEndStation().getCode());

            // Lọc các Route trong khoảng từ startRoute đến endRoute
            List<Route> relevantRoutes = routes.stream()
                    .filter(r -> r.getStationNumber() >= startRoute.getStationNumber() &&
                            r.getStationNumber() <= endRoute.getStationNumber())
                    .sorted((r1, r2) -> Integer.compare(r1.getStationNumber(), r2.getStationNumber()))
                    .collect(Collectors.toList());

            // Tính tổng thời gian di chuyển
            LocalDate currentDate = trainJourney.getDepartureDate();
            Duration totalDuration = Duration.ZERO;

            for (int i = 0; i < relevantRoutes.size() - 1; i++) {
                Route currentRoute = relevantRoutes.get(i);
                Route nextRoute = relevantRoutes.get(i + 1);

                LocalTime departureTime = currentRoute.getDepartureTime();
                LocalTime arrivalTime = nextRoute.getArrivalTime();

                LocalDateTime departureDateTime = LocalDateTime.of(currentDate, departureTime);
                LocalDateTime arrivalDateTime = LocalDateTime.of(currentDate, arrivalTime);

                // Nếu arrivalTime của chặng tiếp theo nhỏ hơn departureTime, điều chỉnh qua đêm
                if (arrivalTime.isBefore(departureTime)) {
                    arrivalDateTime = arrivalDateTime.plusDays(1);
                    currentDate = currentDate.plusDays(1); // Cập nhật ngày cho lần sau
                }

                // Cộng thời gian của chặng hiện tại vào totalDuration
                totalDuration = totalDuration.plus(Duration.between(departureDateTime, arrivalDateTime));
            }

            // Cập nhật arrivalDate và totalDuration vào DTO
            LocalDateTime finalArrivalDateTime = LocalDateTime.of(currentDate, endRoute.getArrivalTime());
            if (endRoute.getArrivalTime().isBefore(relevantRoutes.get(relevantRoutes.size() - 2).getDepartureTime())) {
                finalArrivalDateTime = finalArrivalDateTime.plusDays(1);
            }
            dto.setArrivalDate(finalArrivalDateTime.toLocalDate());
            dto.setTotalDuration(DurationUtils.formatDuration(totalDuration));

            // Tính tổng khoảng cách
            double totalDistance = relevantRoutes.stream()
                    .mapToDouble(Route::getDistance)
                    .sum();
            dto.setTotalDistance(totalDistance);

            // Tính giá vé dựa trên tổng khoảng cách
            dto.setPrices(getSeatTypePrices(totalDistance));
        }

        // Map Carriages to CarriageDTO
        List<CarriageDTO> carriageDTOs = trainJourney.getTrain().getCarriages().stream()
                .map(this::mapToCarriageDTO)
                .collect(Collectors.toList());
        dto.setCarriages(carriageDTOs);

        return dto;
    }

    private List<SeatTypePriceDTO> getSeatTypePrices(double totalDistance) {
        List<SeatType> seatTypes = seatTypeRepository.findAll();

        return seatTypes.stream().map(seatType -> {
            SeatTypePriceDTO priceDTO = new SeatTypePriceDTO();
            priceDTO.setSeatTypeId(seatType.getSeatTypeId());
            priceDTO.setSeatType(seatType.getSeatType());
            priceDTO.setCode(seatType.getCode());
            priceDTO.setDescription(seatType.getDescription());

            // Calculate total price using the Price entity's method if available
            if (seatType.getPrice() != null) {
                priceDTO.setTotalPrice(seatType.getPrice().calTotalPrice(totalDistance));
            } else {
                priceDTO.setTotalPrice(null); // Or some default/fallback value if needed
            }

            return priceDTO;
        }).collect(Collectors.toList());
    }

    private CarriageDTO mapToCarriageDTO(Carriage carriage) {
        CarriageDTO dto = new CarriageDTO();
        dto.setCarriageId(carriage.getCarriageId());
        dto.setCarriageNumber(carriage.getCarriageNumber());
        dto.setCarriageClassName(carriage.getCarriageClass() != null ? carriage.getCarriageClass().getName() : null);
        dto.setCarriageClassId(carriage.getCarriageClass().getCarriageClassId());
//        dto.setSeatCount(carriage.getSeatCount());
//        dto.setTotalFloors(carriage.getTotalFloors());
        return dto;
    }

    public Duration calculateTotalDuration(String departureCode, String arrivalCode, Integer trainId) {
        List<Route> routes = routeRepository.findRoutesBetweenStations(departureCode, arrivalCode, trainId);

        // Kiểm tra nếu không tìm thấy route
        if (routes.isEmpty()) {
            throw new RuntimeException("No routes found between departure and arrival codes.");
        }

        // Tính tổng thời gian
        LocalTime totalDepartureTime = routes.get(0).getDepartureTime(); // Thời gian khởi hành đầu tiên
        LocalTime totalArrivalTime = routes.get(routes.size() - 1).getArrivalTime(); // Thời gian đến cuối cùng

        // Tính tổng thời gian
        Duration totalDuration = Duration.between(totalDepartureTime, totalArrivalTime);
        return totalDuration;
    }

    public Integer calculateOrderNumber(Train train) {
        // Lấy tất cả các carriage của tàu này
        List<Carriage> carriages = carriageRepository.findByTrain(train);

        if (carriages.isEmpty()) {
            return 1;  // Nếu không có toa nào, đặt orderNumber là 1
        }

        // Lấy orderNumber của toa cuối cùng, cộng thêm 1
        Carriage lastCarriage = carriages.stream()
                .max(Comparator.comparingInt(Carriage::getOrderNumber))  // Lấy toa có orderNumber lớn nhất
                .orElseThrow(() -> new RuntimeException("Failed to find the last carriage"));

        return lastCarriage.getOrderNumber() + 1;  // Cộng thêm 1 vào orderNumber của toa cuối
    }

    @Service
    public static class SeatHoldService {

        private final SeatHoldRepository seatHoldRepository;

        @Autowired
        public SeatHoldService(SeatHoldRepository seatHoldRepository) {
            this.seatHoldRepository = seatHoldRepository;
        }

        // Create or update a SeatHold
        public SeatHold saveSeatHold(SeatHold seatHold) {
            return seatHoldRepository.save(seatHold);
        }

        // Find a SeatHold by its ID
        public Optional<SeatHold> getSeatHoldById(Integer id) {
            return seatHoldRepository.findById(id);
        }

        // Find all SeatHolds by trainId
        public List<SeatHold> getSeatHoldsByTrainId(Integer trainId) {
            return seatHoldRepository.findByTrainTrainId(trainId);
        }

        // Find all SeatHolds by departure station
        public List<SeatHold> getSeatHoldsByDepartureStation(Integer departureStationId) {
            return seatHoldRepository.findByDepartureStationStationId(departureStationId);
        }

        // Find SeatHolds by status
        public List<SeatHold> getSeatHoldsByStatus(String status) {
            return seatHoldRepository.findByStatus(status);
        }

        // Find SeatHolds that are about to expire
        public List<SeatHold> getExpiredSeatHolds() {
            LocalDateTime now = LocalDateTime.now();
            return seatHoldRepository.findByExpirationTimeBeforeAndStatus(now, "HOLD");
        }

        // Remove a SeatHold by its ID
        public void deleteSeatHold(Integer id) {
            seatHoldRepository.deleteById(id);
        }
    }


}