package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Carriage.CarriageDTO;
import com.trainticketbooking.app.Dtos.Carriage.CarriageWithoutSeatsDTO;
import com.trainticketbooking.app.Dtos.Train.TrainDTO;
import com.trainticketbooking.app.Dtos.Train.TrainWithCarriagesDTO;
import com.trainticketbooking.app.Entities.Carriage;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Train;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainMapper {
    TrainMapper INSTANCE = Mappers.getMapper(TrainMapper.class);

    @Mapping(source = "trainId", target = "trainId")
    @Mapping(source = "trainNumber", target = "trainNumber")
    @Mapping(source = "trainType", target = "trainType")
    TrainDTO toTrainDTO(Train train);

    default TrainDTO mapToTrainDTOWithDetails(Train train) {
        TrainDTO dto = toTrainDTO(train);

        // Tìm route đầu tiên và cuối cùng để lấy thời gian khởi hành và kết thúc
        List<Route> routes = train.getRoutes();
        Route firstRoute = routes.stream().min(Comparator.comparing(Route::getStationNumber)).orElse(null);
        Route lastRoute = routes.stream().max(Comparator.comparing(Route::getStationNumber)).orElse(null);

        if (firstRoute != null && lastRoute != null) {
            dto.setDepartureTime(firstRoute.getDepartureTime());
            dto.setArrivalTime(lastRoute.getArrivalTime());

            // Tính tổng thời gian
            Duration totalDuration = Duration.between(firstRoute.getDepartureTime(), lastRoute.getArrivalTime());
            dto.setTotalDuration(String.format("%d giờ %d phút",
                    totalDuration.toHours(), totalDuration.toMinutesPart()));

            // Tính tổng khoảng cách
            double totalDistance = routes.stream().mapToDouble(Route::getDistance).sum();
            dto.setTotalDistance(totalDistance);
        }

        return dto;
    }

    @Mappings({
            @Mapping(source = "train.trainId", target = "trainId"), // This maps trainId from Train entity
            @Mapping(source = "train.trainNumber", target = "trainNumber"),
            @Mapping(source = "train.trainType", target = "trainType"),
            @Mapping(target = "carriages", source = "carriages")  // This maps the carriages field
    })
    TrainWithCarriagesDTO toTrainWithCarriagesDTO(Train train, List<CarriageWithoutSeatsDTO> carriages);

    @Mappings({
            @Mapping(source = "train.trainId", target = "trainId"), // This maps trainId from Train entity
            @Mapping(source = "train.trainNumber", target = "trainNumber"),
            @Mapping(source = "train.trainType", target = "trainType"),
    })
    TrainWithCarriagesDTO toTrainWithCarriagesDTO(Train train);

}