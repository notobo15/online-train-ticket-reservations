package com.trainticketbooking.app.Services.impl;

import com.trainticketbooking.app.Dtos.Seat.CreateTemporaryTicketHoldDto;
import com.trainticketbooking.app.Dtos.Seat.TemporaryTicketHoldDto;
import com.trainticketbooking.app.Dtos.TemporaryTicketHold.TemporaryTicketHoldDTO;
import com.trainticketbooking.app.Entities.TemporaryTicketHold;
import com.trainticketbooking.app.Mappers.TemporaryTicketHoldMapper;
import com.trainticketbooking.app.Repos.TemporaryTicketHoldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class TemporaryTicketHoldService {

    @Autowired
    private TemporaryTicketHoldRepository temporaryTicketHoldRepository;

    @Autowired
    private TemporaryTicketHoldMapper temporaryTicketHoldMapper;

//    public TemporaryTicketHoldDto holdTicket(CreateTemporaryTicketHoldDto  createTicketHoldDto) {
//        TemporaryTicketHoldDto ticketHoldDto = (TemporaryTicketHoldDto) createTicketHoldDto;
//        ticketHoldDto.setHoldStartTime(LocalDateTime.now());
//        ticketHoldDto.setExpirationTime(ticketHoldDto.getHoldStartTime().plusMinutes(10));
//
//        TemporaryTicketHold ticketHold = temporaryTicketHoldMapper.toEntity(ticketHoldDto);
//        TemporaryTicketHold savedHold = temporaryTicketHoldRepository.save(ticketHold);
//        return temporaryTicketHoldMapper.toDto(savedHold);
//    }
//
//    public Optional<TemporaryTicketHoldDto> findHoldById(Integer holdId) {
//        return temporaryTicketHoldRepository.findById(holdId)
//                .map(temporaryTicketHoldMapper::toDto);
//    }

    public void releaseExpiredHolds() {
        temporaryTicketHoldRepository.deleteByExpirationTimeBefore(LocalDateTime.now());
    }

    public void releaseHold(Integer holdId) {
        temporaryTicketHoldRepository.deleteById(holdId);
    }

    public List<TemporaryTicketHoldDTO> findAll() {
        return temporaryTicketHoldRepository.findAll()
                .stream()
                .map(temporaryTicketHoldMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<TemporaryTicketHoldDTO> findById(Integer id) {
        return temporaryTicketHoldRepository.findById(id).map(temporaryTicketHoldMapper::toDTO);
    }

//    public TemporaryTicketHoldDTO save(TemporaryTicketHoldDTO dto) {
//        TemporaryTicketHold entity = temporaryTicketHoldMapper.toEntity(dto);
//        TemporaryTicketHold savedEntity = temporaryTicketHoldRepository.save(entity);
//        return temporaryTicketHoldMapper.toDTO(savedEntity);
//    }
    public TemporaryTicketHoldDTO save(TemporaryTicketHoldDTO dto) {
        if (isHoldOverlapping(dto)) {
            throw new IllegalArgumentException("Hold đã tồn tại trên tuyến cho ghế và thời gian đã chọn.");
        }
        TemporaryTicketHold entity = temporaryTicketHoldMapper.toEntity(dto);
        TemporaryTicketHold savedEntity = temporaryTicketHoldRepository.save(entity);
        return temporaryTicketHoldMapper.toDTO(savedEntity);
    }


    public void deleteById(Integer id) {
        temporaryTicketHoldRepository.deleteById(id);
    }

    public boolean isHoldOverlapping(TemporaryTicketHoldDTO dto) {
        List<TemporaryTicketHold> existingHolds = temporaryTicketHoldRepository.findBySeat_SeatIdAndTrainIdAndDepartureDate(
                dto.getSeatId(),
                dto.getTrainId(),
                dto.getDepartureDate()
        );

        for (TemporaryTicketHold hold : existingHolds) {
            int existingDeparture = hold.getDepartureStation().getStationId();
            int existingArrival = hold.getArrivalStation().getStationId();
            int newDeparture = dto.getDepartureStationId();
            int newArrival = dto.getArrivalStationId();

            // Kiểm tra xem khoảng giữ chỗ mới có chồng chéo với khoảng đã có
            if (!(newArrival <= existingDeparture || newDeparture >= existingArrival)) {
                return true;
            }
        }
        return false;
    }
}
