package com.trainticketbooking.app.Dtos.Carriage;


import lombok.Data;

@Data
public class CarriageWithoutSeatsDTO {
    private Integer carriageId;
    private String carriageNumber;
    private String carriageClassName;
    private int carriageClassId;
    private int orderNumber;
}
