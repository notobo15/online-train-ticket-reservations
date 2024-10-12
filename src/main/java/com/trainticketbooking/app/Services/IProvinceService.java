package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Province;

public interface IProvinceService extends IService<Province>{
    public boolean existsByProvinceId(Integer provinceId);
}
