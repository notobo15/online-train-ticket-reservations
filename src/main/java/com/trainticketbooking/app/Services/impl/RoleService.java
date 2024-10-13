package com.trainticketbooking.app.Services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trainticketbooking.app.Entities.Role;
import com.trainticketbooking.app.Repos.RoleRepository;
import com.trainticketbooking.app.Services.IRoleService;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> getById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void deleteById(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role update(Role role) {
        Optional<Role> existingRole = roleRepository.findById(role.getRoleId());
        if (existingRole.isPresent()) {
            Role updatedRole = existingRole.get();
            updatedRole.setName(role.getName());
            return roleRepository.save(updatedRole);
        } else {
            throw new RuntimeException("Role not found with ID: " + role.getRoleId());
        }
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
}