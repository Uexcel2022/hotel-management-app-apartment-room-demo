package com.uexcel.apartment.service;

import com.uexcel.apartment.dto.FreeApartmentDto;
import com.uexcel.apartment.dto.ReservationDto;
import com.uexcel.apartment.dto.ReservationResponseDto;
import com.uexcel.apartment.dto.ResponseDto;

import java.util.List;

public interface IReservationService {
    List<FreeApartmentDto> getFreeRoomsByMonth(String monthNAme);
    List<FreeApartmentDto> getFreeRoomsByDays(Integer days);
    ReservationResponseDto saveReservation(ReservationDto reservationDto);
    ResponseDto deletePastReservations();
}
