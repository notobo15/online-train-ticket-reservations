package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.CarriageClass;
import com.trainticketbooking.app.Entities.Seat;
import com.trainticketbooking.app.Repos.CarriageClassRepository;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Services.ICarriageClassService;
import com.trainticketbooking.app.Services.ICarriageService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarriageClassService implements ICarriageClassService {

    @Autowired
    private CarriageClassRepository carriageClassRepository;

    @Override
    public List<CarriageClass> getAll() {
        return carriageClassRepository.findAll();
    }

    @Override
    public Optional<CarriageClass> getById(Integer id) {
        return carriageClassRepository.findById(id);
    }

    @Override
    public CarriageClass save(CarriageClass carriage) {
        return carriageClassRepository.save(carriage);
    }

    @Override
    public void deleteById(Integer id) {
        carriageClassRepository.deleteById(id);
    }

    @Override
    public CarriageClass update(CarriageClass carriage) {
        // Tìm kiếm đối tượng CarriageClass bằng ID hoặc ném ngoại lệ nếu không tìm thấy
        CarriageClass existingCarriage = carriageClassRepository.findById(carriage.getCarriageClassId())
                .orElseThrow(() -> new EntityNotFoundException("Carriage class not found with ID: " + carriage.getCarriageClassId()));

        // Cập nhật thông tin
        existingCarriage.setName(carriage.getName());

        // Lưu và trả về đối tượng đã cập nhật
        return carriageClassRepository.save(existingCarriage);
    }

}