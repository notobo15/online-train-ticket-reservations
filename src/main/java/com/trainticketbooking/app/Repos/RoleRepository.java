package com.trainticketbooking.app.Repos;

import com.trainticketbooking.app.Entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}