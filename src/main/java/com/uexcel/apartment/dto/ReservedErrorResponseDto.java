package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Schema(name ="ReservedErrorResponse",description = "This Schema will hold error details.")
public class ReservedErrorResponseDto {
    private  String timestamp;
    private int status;
    private  String error;
    private  String message;
    private List<DateApartmentsDto> dateRooms;
    private  String apiPath;
}
