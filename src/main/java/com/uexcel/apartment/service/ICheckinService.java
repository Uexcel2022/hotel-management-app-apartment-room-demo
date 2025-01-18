package com.uexcel.apartment.service;

import com.uexcel.apartment.dto.CheckinRequestDto;
import com.uexcel.apartment.dto.CheckinResponseDto;
import com.uexcel.apartment.dto.ResponseDto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface ICheckinService {
    ResponseDto checkin(CheckinRequestDto checkinRequestDto);
    ResponseDto checkout(Long checkinId);
    List<CheckinResponseDto> getCheckin(String roomNumber);

    static  String getTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
