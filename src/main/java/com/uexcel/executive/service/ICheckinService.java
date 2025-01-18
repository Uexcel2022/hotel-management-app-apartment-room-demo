package com.uexcel.executive.service;

import com.uexcel.executive.dto.CheckinRequestDto;
import com.uexcel.executive.dto.CheckinResponseDto;
import com.uexcel.executive.dto.ResponseDto;

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
