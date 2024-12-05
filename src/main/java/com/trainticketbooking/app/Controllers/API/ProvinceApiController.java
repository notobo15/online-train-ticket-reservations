package com.trainticketbooking.app.Controllers.API;

import com.trainticketbooking.app.Dtos.Booking.BookingResponseDTO;
import com.trainticketbooking.app.Dtos.Province.ProvinceDTO;
import com.trainticketbooking.app.Dtos.Station.StationDTO;
import com.trainticketbooking.app.Dtos.Wrappers.ApiResponse;
import com.trainticketbooking.app.Entities.Province;
import com.trainticketbooking.app.Entities.Station;
import com.trainticketbooking.app.Mappers.ProvinceMapper;
import com.trainticketbooking.app.Services.IProvinceService;
import com.trainticketbooking.app.Services.Redis.ProvinceRedisService;
import com.trainticketbooking.app.Services.impl.ProvinceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/provinces")
public class ProvinceApiController {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProvinceRedisService provinceRedisService;

    @GetMapping("/with-stations")
    public  ApiResponse<List<ProvinceDTO>> getProvincesWithStations() {
        List<ProvinceDTO> provincesWithStations = provinceRedisService.getProvincesWithStations("provincesWithStations");
        if (provincesWithStations == null) {
            provincesWithStations = provinceService.getProvincesWithStations();
            provinceRedisService.saveProvincesWithStations("provincesWithStations", provincesWithStations);
        }
        return ApiResponse.<List<ProvinceDTO>>builder()
                .result(provincesWithStations)
                .build();
    }

    private ProvinceDTO convertToDTO(Province province) {
        ProvinceDTO provinceDTO = modelMapper.map(province, ProvinceDTO.class);
        List<StationDTO> stationDTOs = province.getStations().stream()
                .map(this::convertStationToDTO)
                .collect(Collectors.toList());
        provinceDTO.setStations(stationDTOs);
        return provinceDTO;
    }

    private StationDTO convertStationToDTO(Station station) {
        return modelMapper.map(station, StationDTO.class);
    }
}