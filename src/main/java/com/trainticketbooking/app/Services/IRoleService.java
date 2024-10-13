package com.trainticketbooking.app.Services;

import com.trainticketbooking.app.Entities.Role;

public interface IRoleService extends IService<Role> {
    Role findByName(String name);
}