package com.uexcel.executive.service.impl;

import com.uexcel.executive.constants.Constants;
import com.uexcel.executive.dto.*;
import com.uexcel.executive.exception.AppExceptions;
import com.uexcel.executive.mapper.CheckinMapper;
import com.uexcel.executive.model.Checkin;
import com.uexcel.executive.model.Apartment;
import com.uexcel.executive.model.ReservationDates;
import com.uexcel.executive.persistence.CheckinRepository;
import com.uexcel.executive.persistence.ExecutiveRoomRepository;
import com.uexcel.executive.service.ICheckinService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class ICheckinImpl implements ICheckinService {
    private  final CheckinMapper checkinMapper;
    private final CheckinRepository checkinRepository;
    private  final ExecutiveRoomRepository executiveRoomRepository;
    @Override
    public ResponseDto checkin(CheckinRequestDto checkinRequestDto) {
        Checkin inUse = checkinRepository
                .findByExecutiveRoom_RoomNumberAndDateOut(checkinRequestDto.getApartmentCode(), null);
        if (inUse != null) {
            throw new AppExceptions(
                    HttpStatus.BAD_REQUEST.value(),
                    Constants.BadRequest, String.format("Room %s is in use.", checkinRequestDto.getApartmentCode())
            );
        }
        /*
           checking number of days intended to be checked in and checking if  is free
            or the customer actually have reservations for the number days intended.
         */
        List<LocalDate> intendedCheckinDates = new ArrayList<>();
        int checkinNumberOfDays = checkinRequestDto.getNumberOfDays();
        for (int i = 0; i < checkinNumberOfDays; i++) {
            intendedCheckinDates.add(LocalDate.now().plusDays(i));
        }

        List<ReservationDates> rsvDates = executiveRoomRepository
                .findByRoomNumberJpql(checkinRequestDto.getApartmentCode());

        Apartment room = null;
        for (LocalDate checkinDate : intendedCheckinDates) {
            if (!rsvDates.isEmpty()) {
                room = rsvDates.getFirst().getApartment();  //getting the room details.
                ReservationDates rsvDate =
                                rsvDates.stream().filter(v -> v.getDate().equals(checkinDate)).findFirst().orElse(null);
                if (rsvDate != null) {
                    if (!rsvDate.getReservation().getPhone().equals(checkinRequestDto.getPhone())) {
                        throw new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                                String.format("Room %s is on reservation this date: %s", checkinRequestDto.getApartmentCode(), checkinDate));
                    }
                }
            } else {
                room = executiveRoomRepository.findByRoomNumber(checkinRequestDto.getApartmentCode());
                if (room == null) {
                    throw new AppExceptions(HttpStatus.NOT_FOUND.value(), Constants.NotFound,
                            String.format("No regular room with roomNumber: %s ", checkinRequestDto.getApartmentCode()));
                }
            }
        }

        Checkin checkin = checkinMapper.toCheckin(new Checkin(), checkinRequestDto);
        checkin.setAmount(room.getPrice());
        checkin.setApartment(room);
        Checkin savedCheckin =  checkinRepository.save(checkin);
        if(savedCheckin.getId() == null) {
            throw  new AppExceptions(
                    HttpStatus.EXPECTATION_FAILED.value(),Constants.Failed,"Checkin failed."
            );
        }
        return new ResponseDto(HttpStatus.CREATED.value(), Constants.Created,"Checkin  successfully.");
    }

    @Override
    public ResponseDto checkout(Long checkinId) {
        Checkin toUpdate = checkinRepository.findById(checkinId)
                .orElseThrow(() -> new AppExceptions(HttpStatus.BAD_REQUEST.value(),
                    Constants.BadRequest, String.format("No checkin found for CheckId:  %s", checkinId)));

        if(toUpdate.getDateOut()!=null){
            throw  new AppExceptions(
                    HttpStatus.BAD_REQUEST.value(),
                    Constants.BadRequest,String.format("Room %s is not in use.",
                    toUpdate.getApartment().getRoomNumber())
            );
        }
        toUpdate.setDateOut(ICheckinService.getTime());
       Checkin update = checkinRepository.save(toUpdate);
       if(update.getDateOut()==null){
           throw  new AppExceptions(
                   HttpStatus.EXPECTATION_FAILED.value(),Constants.Failed,"Checkout failed."
           );
       }
        return new ResponseDto(HttpStatus.OK.value(), Constants.OK,"Checkout successfully.");
    }

    @Override
    public List<CheckinResponseDto> getCheckin(String roomNumber) {
        if(roomNumber != null) {
            if(!executiveRoomRepository.existsByRoomNumber(roomNumber)) {
                throw new AppExceptions(HttpStatus.NOT_FOUND.value(),
                        Constants.NotFound,String.format("No room found with roomNumber: %s", roomNumber)
                );
            }
         Checkin toCheckoutRoom =  checkinRepository
                 .findByExecutiveRoom_RoomNumberAndDateOut(roomNumber,null);
                 if(toCheckoutRoom == null) {
                     throw new AppExceptions(HttpStatus.BAD_REQUEST.value(),
                             Constants.BadRequest, String.format("Room %s is not in use.", roomNumber));
                 }
          return  List.of(checkinMapper.toCheckinResponseDto(toCheckoutRoom,new CheckinResponseDto()));
        }
        List<Checkin> checkins = checkinRepository.findByDateOut(null);
        List<CheckinResponseDto> checkinResponseDtoList = new ArrayList<>();
        if(checkins == null) {
            throw  new AppExceptions(
                    HttpStatus.NOT_FOUND.value(),
                    Constants.NotFound,"No checkin found."
            );
        }
        checkins.forEach(checkin -> checkinResponseDtoList
                .add(checkinMapper.toCheckinResponseDto(checkin,new CheckinResponseDto())));
        return checkinResponseDtoList;
    }
}
