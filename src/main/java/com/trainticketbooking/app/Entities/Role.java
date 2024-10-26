package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Role name cannot be blank")
    @Pattern(regexp = "^ROLE_.*", message = "Role name must start with 'ROLE_'")
    private String name;
}