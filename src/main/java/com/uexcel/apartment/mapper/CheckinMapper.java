package com.uexcel.apartment.mapper;

import com.uexcel.apartment.dto.CheckinRequestDto;
import com.uexcel.apartment.dto.CheckinResponseDto;
import com.uexcel.apartment.model.Checkin;
import org.springframework.stereotype.Component;

import static com.uexcel.apartment.service.ICheckinService.getTime;

@Component
public class CheckinMapper {
    public Checkin toCheckin(Checkin checkin, CheckinRequestDto checkinRequestDto) {
        checkin.setName(checkinRequestDto.getName());
        checkin.setPhone(checkinRequestDto.getPhone());
        checkin.setNumberOfDays(checkinRequestDto.getNumberOfDays());
        checkin.setDateIn(getTime());
        return checkin;
    }

    public CheckinResponseDto toCheckinResponseDto(Checkin checkin, CheckinResponseDto checkinResponseDto) {
        checkinResponseDto.setId(checkin.getId());
        checkinResponseDto.setApartmentCode(checkin.getApartment().getApartmentCode());
        checkinResponseDto.setDateIn(checkin.getDateIn());
        checkinResponseDto.setDateOut(checkin.getDateOut());
        checkinResponseDto.setName(checkin.getName());
        checkinResponseDto.setPhone(checkin.getPhone());
        checkinResponseDto.setAmount(checkin.getAmount());
        checkinResponseDto.setNumberOfDays(checkin.getNumberOfDays());
        return checkinResponseDto;
    }
}
