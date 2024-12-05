package com.trainticketbooking.app.Services.Redis;

import com.trainticketbooking.app.Dtos.Province.ProvinceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ProvinceRedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveProvincesWithStations(String key, List<ProvinceDTO> provincesWithStations) {
        redisTemplate.opsForValue().set(key, provincesWithStations);
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
    }

    public List<ProvinceDTO> getProvincesWithStations(String key) {
        return (List<ProvinceDTO>) redisTemplate.opsForValue().get(key);
    }
}
