package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Repos.CarriageRepository;
import com.trainticketbooking.app.Repos.ProvinceRepository;
import com.trainticketbooking.app.Repos.SeatRepository;
import com.trainticketbooking.app.Services.ICarriageService;
import com.trainticketbooking.app.Services.IProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProvinceService implements IProvinceService {


    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public List<Province> getAll() {
        // Lấy tất cả các Province
        return provinceRepository.findAll();
    }

    @Override
    public Optional<Province> getById(Integer id) {
        // Lấy Province theo ID (nếu có)
        return provinceRepository.findById(id);
    }

    @Override
    public Province save(Province province) {
        // Lưu Province vào cơ sở dữ liệu
        return provinceRepository.save(province);
    }

    @Override
    public void deleteById(Integer id) {
        // Xóa Province theo ID
        provinceRepository.deleteById(id);
    }

    @Override
    public Province update(Province province) {
        Optional<Province> existingProvince = provinceRepository.findById(province.getProvinceId());

        if (existingProvince.isPresent()) {
            Province updatedProvince = existingProvince.get();
            updatedProvince.setName(province.getName());
            updatedProvince.setFullName(province.getFullName());
            updatedProvince.setCodeName(province.getCodeName());
            return provinceRepository.save(updatedProvince);
        } else {
            throw new RuntimeException("Province not found with code: " + province.getProvinceId());
        }
    }

    @Override
    public boolean existsByProvinceId(Integer provinceId) {
        return provinceRepository.existsByProvinceId(provinceId);
    }
}