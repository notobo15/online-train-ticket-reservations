package com.trainticketbooking.app.Dtos;

import com.trainticketbooking.app.Entities.Province;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StationDto {
    Integer stationId;
    String stationName;
    String provinceCode;
    BigDecimal latitude; // Vĩ độ
    BigDecimal longitude; // Kinh độ
    String provinceFullName;
}
