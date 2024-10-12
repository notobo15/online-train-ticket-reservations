package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {
    boolean existsByProvinceId(Integer provinceId);
}