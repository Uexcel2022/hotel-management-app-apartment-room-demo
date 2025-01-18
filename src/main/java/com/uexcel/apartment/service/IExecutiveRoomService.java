package com.uexcel.apartment.service;

import com.uexcel.apartment.dto.AvailableApartmentDto;
import com.uexcel.apartment.dto.ReservedRoomInFoDto;

import java.util.List;
import java.util.Map;

public interface IExecutiveRoomService {
    ReservedRoomInFoDto getRegularRoomByRoomNumber(String roomNumber);
    Map<String, List<AvailableApartmentDto>> getFreeRoomsByDays(Integer numberOfDays);
    Map<String,List<AvailableApartmentDto>> getFreeRoomsByMonth(String monthName);
}
