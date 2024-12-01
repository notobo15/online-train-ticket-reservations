package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProvinceService extends IService<Province>{
    public boolean existsByProvinceId(Integer provinceId);
    Page<Province> findAll(Pageable pageable);
    public Province adminUpdateProvince(Province province);

}
