package com.uexcel.apartment.exception;

import com.uexcel.apartment.dto.DateApartmentsDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ReservedRoomException extends RuntimeException {
    private List<DateApartmentsDto> dateApartments;
    public ReservedRoomException (List<DateApartmentsDto> dateApartments) {
        super("Reserved Room(s) : ");
        this.dateApartments = dateApartments;
    }
}
