package com.uexcel.executive.exception;

import com.uexcel.executive.dto.DateRoomsDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ReservedRoomException extends RuntimeException {
    private List<DateRoomsDto> dateRooms;
    public ReservedRoomException (List<DateRoomsDto> dates) {
        super("Reserved Room(s) : ");
        this.dateRooms = dates;
    }
}
