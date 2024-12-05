package com.trainticketbooking.app.Services.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Lưu trữ một giá trị vào Redis
    public void saveData(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Lấy giá trị từ Redis
    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Xóa giá trị khỏi Redis
    public void deleteData(String key) {
        redisTemplate.delete(key);
    }
}
