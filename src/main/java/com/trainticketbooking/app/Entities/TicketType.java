package com.trainticketbooking.app.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ticket_types")
@Data
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_type_id")
    private Integer ticketTypeId;

    @Column(name = "name", nullable = false)
    private String name;
}