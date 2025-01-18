package com.uexcel.executive.service;

import com.uexcel.executive.dto.FreeRoomsDto;
import com.uexcel.executive.dto.ReservationDto;
import com.uexcel.executive.dto.ReservationResponseDto;
import com.uexcel.executive.dto.ResponseDto;

import java.util.List;

public interface IReservationService {
    List<FreeRoomsDto> getFreeRoomsByMonth(String monthNAme);
    List<FreeRoomsDto> getFreeRoomsByDays(Integer days);
    ReservationResponseDto saveReservation(ReservationDto reservationDto);
    ResponseDto deletePastReservations();
}
