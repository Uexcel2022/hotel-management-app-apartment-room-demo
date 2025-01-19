package com.uexcel.apartment.service.impl;

import com.uexcel.apartment.constants.Month;
import com.uexcel.apartment.dto.AvailableApartmentDto;
import com.uexcel.apartment.dto.ReservedRoomInFoDto;
import com.uexcel.apartment.exception.AppExceptions;
import com.uexcel.apartment.mapper.GetRsvByApartmentCodeMapper;
import com.uexcel.apartment.model.Apartment;
import com.uexcel.apartment.model.ReservationDates;
import com.uexcel.apartment.persistence.ApartmentRepository;
import com.uexcel.apartment.service.IExecutiveRoomService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class IExecutiveRoomServiceImpl implements IExecutiveRoomService {
    private final ApartmentRepository apartmentRepository;
    private final GetRsvByApartmentCodeMapper rsvMapper;
    private final Month month;
    @Override
    public ReservedRoomInFoDto getRegularRoomByRoomNumber(String roomNumber) {
        List<ReservationDates> regularRoom =
                apartmentRepository.findByApartmentCodeJpql(roomNumber);
           return   rsvMapper.toReservedRoomInFoDto(regularRoom,new ReservedRoomInFoDto());
    }

    @Override
    public Map<String, List<AvailableApartmentDto>> getFreeRoomsByDays(Integer numberOfDays) {
        List<AvailableApartmentDto> availableApartmentDto = new ArrayList<>();
        List<LocalDate> dates = new ArrayList<>();
        if (numberOfDays == null) {
            numberOfDays = 7;
        }
       /*
            Creating list of dates based on the desired number of days
        */
        for (int i = 0; i < numberOfDays; i++) {
            dates.add(LocalDate.now().plusDays(i));
        }
        dates.sort(Comparator.naturalOrder());
        List<Apartment> apartments = apartmentRepository.findAll();
        apartments.forEach(regularRoom -> {
            List<ReservationDates> reservationDates = regularRoom.getReservationDates();
            if (reservationDates.isEmpty()) {
                for (LocalDate localDate : dates) {
                    availableApartmentDto.add(new AvailableApartmentDto(localDate,
                            regularRoom.getApartmentCode()));
                }
            }
            if(!regularRoom.getReservationDates().isEmpty()) {
                for (LocalDate localDate : dates) {
                    if (regularRoom.getReservationDates().stream().noneMatch(v -> v.getDate().equals(localDate)) ) {
                        availableApartmentDto.add(new AvailableApartmentDto(localDate,
                                regularRoom.getApartmentCode()));
                    }

                }
            }
        });

        availableApartmentDto.sort(Comparator.comparing(AvailableApartmentDto::getDate));

             return   availableApartmentDto.stream().
                     collect(groupingBy(AvailableApartmentDto::getApartmentCode, LinkedHashMap::new,toList()));
    }

    @Override
    public Map<String, List<AvailableApartmentDto>> getFreeRoomsByMonth(String monthName) {
        List<LocalDate> dates = new ArrayList<>();
        List<AvailableApartmentDto> availableApartmentDto = new ArrayList<>();
        LocalDate monthDate;
        if (monthName == null) {
            monthDate = month.getStartDate(String.valueOf(LocalDate.now().getMonth()));
        }else {
            monthDate = month.getStartDate(monthName.toUpperCase());
        }
        LocalDate now = LocalDate.now();
        if(monthDate.getMonth().equals("DECEMBER") && monthName != null
                && monthName.equalsIgnoreCase("JANUARY")){
            monthDate =  LocalDate.of(now.getYear()+1,1,1);
        }
        if(monthDate.getDayOfYear() < now.getDayOfYear() && monthDate.getYear()==now.getYear()
                && ! monthDate.getMonth().equals(now.getMonth())){
            throw new AppExceptions(HttpStatus.BAD_REQUEST.value(),
                    "Bad Request","The desired month is not available."
            );
        }
       /*
            Creating list of dates based on the desired  month
        */
        if(monthDate.getMonth().equals(LocalDate.now().getMonth()) && monthDate.getYear()==LocalDate.now().getYear()){
            for (int i = 0; i < LocalDate.now().lengthOfMonth(); i++) {
                if(monthDate.plusDays(i).isAfter(LocalDate.now())||monthDate.plusDays(i).equals(LocalDate.now())) {
                    dates.add(monthDate.plusDays(i));
                }
            }
        } else {
            for (int i = 0; i < monthDate.lengthOfMonth(); i++) {
                dates.add(monthDate.plusDays(i));
            }
        }
        dates.sort(Comparator.naturalOrder());
        List<Apartment> apartments = apartmentRepository.findAll();
        apartments.forEach(regularRoom -> {
            List<ReservationDates> reservationDates = regularRoom.getReservationDates();
            if (reservationDates.isEmpty() ) {
                for (LocalDate localDate : dates) {
                    availableApartmentDto.add(new AvailableApartmentDto(localDate,
                            regularRoom.getApartmentCode()));
                }
            }
            if(!regularRoom.getReservationDates().isEmpty()) {
                for (LocalDate localDate : dates) {
                    if (regularRoom.getReservationDates().stream().noneMatch(v -> v.getDate().equals(localDate)) ) {
                        availableApartmentDto.add(new AvailableApartmentDto(localDate,
                                regularRoom.getApartmentCode()));
                    }
                }
            }
        });

        availableApartmentDto.sort(Comparator.comparing(AvailableApartmentDto::getDate));

           return   availableApartmentDto.stream()
                   .collect(groupingBy(AvailableApartmentDto::getApartmentCode, LinkedHashMap::new,toList()));

    }



}
