package com.uexcel.apartment.service;

import com.uexcel.apartment.dto.FreeApartmentDto;
import com.uexcel.apartment.dto.ReservationDto;
import com.uexcel.apartment.dto.ReservationResponseDto;
import com.uexcel.apartment.dto.ResponseDto;

import java.util.List;

public interface IReservationService {
    List<FreeApartmentDto> getFreeApartmentByMonth(String monthNAme,String apartmentCode);
    List<FreeApartmentDto> getFreeApartmentByDays(Integer days,String apartmentCode);
    ReservationResponseDto saveReservation(ReservationDto reservationDto);
    ResponseDto deletePastReservations();
}
