package com.uexcel.executive.mapper;

import com.uexcel.executive.dto.ReservationDatesDto;
import com.uexcel.executive.dto.ReservationDetailsDto;
import com.uexcel.executive.dto.ReservationInfoDto;
import com.uexcel.executive.dto.ReservedRoomInFoDto;
import com.uexcel.executive.model.Apartment;
import com.uexcel.executive.model.Reservation;
import com.uexcel.executive.model.ReservationDates;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetRsvByRoomNumberMapper {
    /*
         mapping reservation dates fetched by room number to ReservedRoomInFoDto
    */
    public ReservedRoomInFoDto toReservedRoomInFoDto(
            List<ReservationDates> reservationDates, ReservedRoomInFoDto reservedRoomInFoDto) {
            reservationDates.forEach(reservationDate -> {
                Reservation rsv = reservationDate.getReservation();
                Apartment room = reservationDate.getApartment();
                reservedRoomInFoDto.setRoomNumber(room.getRoomNumber());
                reservedRoomInFoDto.setPrice(room.getPrice());
                reservedRoomInFoDto.setId(room.getId());
                ReservationInfoDto rsvInfo = new
                        ReservationInfoDto(rsv.getId(),rsv.getName(),rsv.getPhone(),rsv.getDescription());
                ReservationDatesDto rsvDates =
                        new ReservationDatesDto(reservationDate.getId(),reservationDate.getDate());
                reservedRoomInFoDto.getReservationDetails()
                        .add(new ReservationDetailsDto(rsvDates,rsvInfo));
            });
            return reservedRoomInFoDto;
    }
}
