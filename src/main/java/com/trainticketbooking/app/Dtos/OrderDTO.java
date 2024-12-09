package com.trainticketbooking.app.Dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.trainticketbooking.app.Enums.OrderIntent;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderDTO implements Serializable {
    private OrderIntent intent;
//    @JsonProperty("purchase_units")
//    private List<PurchaseUnit> purchaseUnits;
    @JsonProperty("application_context")
    private PayPalAppContextDTO applicationContext;
}
