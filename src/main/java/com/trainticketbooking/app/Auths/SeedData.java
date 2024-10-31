package com.trainticketbooking.app.Auths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.trainticketbooking.app.Entities.Role;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Services.impl.RoleService;
import com.trainticketbooking.app.Services.impl.UserService;

import jakarta.annotation.PostConstruct;


@Component
public class SeedData {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void seed() {
        if (roleService.getAll().isEmpty()) {
            Role adminRole = new Role();
            adminRole.setRoleId(1);
            adminRole.setName("ROLE_ADMIN");

            Role userRole = new Role();
            userRole.setRoleId(2);
            userRole.setName("ROLE_USER");

            Role managerRole = new Role();
            managerRole.setRoleId(3);
            managerRole.setName("ROLE_MANAGER");

            roleService.save(adminRole);
            roleService.save(userRole);
            roleService.save(managerRole);
        }
        if (userService.findByUsername("user") == null) {
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("1234"));
            user.setEmail("user@example.com");
            user.setFullName("User");

            Role userRole = roleService.findByName("ROLE_USER");
            if (userRole != null) {
                user.setRole(userRole);
            }

            userService.save(user);
        }
        if (userService.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("1234"));
            adminUser.setEmail("admin@example.com");
            adminUser.setFullName("Admin User");

            Role adminRole = roleService.findByName("ROLE_ADMIN");
            if (adminRole != null) {
                adminUser.setRole(adminRole);
            }
            userService.save(adminUser);
        }
    }
}
