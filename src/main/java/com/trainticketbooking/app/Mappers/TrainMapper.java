package com.trainticketbooking.app.Mappers;

import com.trainticketbooking.app.Dtos.Train.TrainDTO;
import com.trainticketbooking.app.Entities.Route;
import com.trainticketbooking.app.Entities.Train;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;


@Mapper
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
}