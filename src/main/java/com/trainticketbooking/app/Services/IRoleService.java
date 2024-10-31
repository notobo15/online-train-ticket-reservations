package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRoleService extends IService<Role> {
    Role findByName(String name);

    Page<Role> findAll(Pageable pageable);
}