package com.uexcel.apartment.service;

import com.uexcel.apartment.dto.AvailableApartmentDto;
import com.uexcel.apartment.dto.ReservedRoomInFoDto;

import java.util.List;
import java.util.Map;

public interface IApartmentService {
    ReservedRoomInFoDto getApartmentByApartmentNumber(String roomNumber);
    Map<String, List<AvailableApartmentDto>> getFreeApartmentByDays(Integer numberOfDays,String apartmentCode);
    Map<String,List<AvailableApartmentDto>> getFreeApartmentByMonth(String monthName,String apartmentCode);
}
