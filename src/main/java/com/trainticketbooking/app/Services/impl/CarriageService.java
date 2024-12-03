package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.Seat.CarriageSeatMappingDTO;
import com.trainticketbooking.app.Dtos.Seat.SeatDTO;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageSeatMapping;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Entities.Train;
import com.trainticketbooking.app.Mappers.CarriageMapper;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.CarriageSeatMappingRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Repos.TrainRepository;
import com.trainticketbooking.app.Services.ICarriageService;
import com.trainticketbooking.app.Services.ITrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarriageService implements ICarriageService {

    @Autowired
    private CarriageRepository carriageRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private TrainRepository trainRepository;

    @Autowired
    private CarriageSeatMappingRepository carriageSeatMappingRepository;

    @Override
    public List<Carriage> getAll() {
        return carriageRepository.findAll();
    }

    @Override
    public Optional<Carriage> getById(Integer id) {
        return carriageRepository.findById(id);
    }

    @Override
    public Carriage save(Carriage carriage) {
        return carriageRepository.save(carriage);
    }

    @Override
    public void deleteById(Integer id) {
        carriageRepository.deleteById(id);
    }

    @Override
    public Carriage update(Carriage carriage) {
        Optional<Carriage> existingCarriage = carriageRepository.findById(carriage.getCarriageId());
        if (existingCarriage.isPresent()) {
            Carriage updatedCarriage = existingCarriage.get();
            updatedCarriage.setTrain(carriage.getTrain());
            updatedCarriage.setCarriageClass(carriage.getCarriageClass());
            updatedCarriage.setCarriageNumber(carriage.getCarriageNumber());
//            updatedCarriage.setSeatCount(carriage.getSeatCount());
//            updatedCarriage.setTotalFloors(carriage.getTotalFloors());
            return carriageRepository.save(updatedCarriage);
        } else {
            throw new RuntimeException("Carriage not found with ID: " + carriage.getCarriageId());
        }
    }
    @Override
    public Page<Carriage> findAll(Pageable pageable) {
        return carriageRepository.findAll(pageable);
    }

    @Override
    public List<Carriage> findByTrain(Train train) {
        return List.of();
    }


//    public List<Seat> findSeatsByCarriageId(Integer carriageId) {
//        Carriage carriage = carriageRepository.findById(carriageId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid carriage ID: " + carriageId));
//
//        return seatRepository.findByCarriage(carriage);
//    }

    public List<CarriageDTO> searchCarriagesByTrain(Integer trainId) {
        // Tìm Train dựa trên trainId
        Train train = trainRepository.findById(trainId)
                .orElseThrow(() -> new RuntimeException("Train not found"));

        // Lấy danh sách các toa từ Train
        return train.getCarriages().stream()
                .map(CarriageMapper.INSTANCE::toCarriageDTO)
                .collect(Collectors.toList());
    }

//    public List<CarriageSeatMappingDTO> getSeatMappings(Integer carriageId) {
//        List<CarriageSeatMapping> seatMappings = carriageSeatMappingRepository.findByCarriageCarriageId(carriageId);
//
//        return seatMappings.stream().map(seatMapping -> {
//            CarriageSeatMappingDTO dto = new CarriageSeatMappingDTO();
//            dto.setSeatId(seatMapping.getSeat().getSeatId());
//            dto.setSeatNumber(seatMapping.getSeat().getSeatNumber());
//            dto.setSeatType(seatMapping.getSeat().getSeatType().getSeatType());
//            dto.setStatus(seatMapping.getStatus());
//            return dto;
//        }).collect(Collectors.toList());
//    }


    //
    public CarriageDTO getCarriageById(Integer carriageId) {
        Carriage carriage = carriageRepository.findById(carriageId)
                .orElseThrow(() -> new RuntimeException("Carriage not found with id: " + carriageId));

        // Lấy thông tin CarriageDTO
        CarriageDTO dto = mapToCarriageDTO(carriage);

        // Lấy danh sách seats liên kết với Carriage
        List<Seat> seatMappings = seatRepository.findByCarriageCarriageId(carriageId);
        List<SeatDTO> seats = seatMappings.stream()
                .map(this::mapToSeatDTO)
                .collect(Collectors.toList());

        // Gán seats vào CarriageDTO
        dto.setSeats(seats);

        return dto;
    }

    public int getSeatCountByCarriageId(Integer carriageId) {
        return carriageSeatMappingRepository.countByCarriageCarriageId(carriageId); // Giả sử bạn có repository này
    }

    // Phương thức mapToCarriageDTO để chuyển từ Carriage sang CarriageDTO
    private CarriageDTO mapToCarriageDTO(Carriage carriage) {
        CarriageDTO dto = new CarriageDTO();
        dto.setCarriageId(carriage.getCarriageId());
        dto.setCarriageNumber(carriage.getCarriageNumber());
        dto.setCarriageClassName(carriage.getCarriageClass().getName());
        dto.setCarriageClassId(carriage.getCarriageClass().getCarriageClassId());
//        dto.setSeatCount(carriage.getSeatCount());
        return dto;
    }

    private CarriageSeatMappingDTO mapToCarriageSeatMappingDTO(CarriageSeatMapping seatMapping) {
        CarriageSeatMappingDTO dto = new CarriageSeatMappingDTO();
        dto.setCarriageSeatId(seatMapping.getCarriageSeatId());
        dto.setSeatId(seatMapping.getCarriageSeatId());
        dto.setSeatNumber(seatMapping.getSeat().getSeatNumber());
        dto.setStatus(seatMapping.getStatus());
        dto.setSeatType(seatMapping.getSeat().getSeatType().getSeatType()); // giả sử seatType có thuộc tính name
        return dto;
    }

    private SeatDTO mapToSeatDTO(Seat seatMapping) {
        SeatDTO dto = new SeatDTO();
        dto.setSeatId(seatMapping.getSeatId());
        dto.setSeatNumber(seatMapping.getSeatNumber());
        dto.setStatus(seatMapping.getStatus());
        dto.setSeatType(seatMapping.getSeatType().getSeatType()); // giả sử seatType có thuộc tính name
        return dto;
    }

    @Override
    public List<Carriage> findByTrainTrainId(Integer train) {
        return carriageRepository.findByTrainTrainId(train);
    }
}