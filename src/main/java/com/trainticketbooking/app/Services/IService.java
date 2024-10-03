package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IService<T> {
    public List<T> getAll();
    public Optional<T> getById(Integer id);
    public T save(T t);
    public void deleteById(Integer id);
    public T update(T t);
}
