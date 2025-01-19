package com.uexcel.apartment.mapper;

import com.uexcel.apartment.dto.ReservationDatesDto;
import com.uexcel.apartment.dto.ReservationDetailsDto;
import com.uexcel.apartment.dto.ReservationInfoDto;
import com.uexcel.apartment.dto.ReservedRoomInFoDto;
import com.uexcel.apartment.model.Apartment;
import com.uexcel.apartment.model.Reservation;
import com.uexcel.apartment.model.ReservationDates;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetRsvByApartmentCodeMapper {
    /*
         mapping reservation dates fetched by room number to ReservedRoomInFoDto
    */
    public ReservedRoomInFoDto toReservedRoomInFoDto(
            List<ReservationDates> reservationDates, ReservedRoomInFoDto reservedRoomInFoDto) {
            reservationDates.forEach(reservationDate -> {
                Reservation rsv = reservationDate.getReservation();
                Apartment room = reservationDate.getApartment();
                reservedRoomInFoDto.setApartmentCode(room.getApartmentCode());
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
