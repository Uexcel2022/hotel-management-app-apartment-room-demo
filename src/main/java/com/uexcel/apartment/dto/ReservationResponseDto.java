package com.uexcel.apartment.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Schema(name ="ReservationResponse",description = "This Schema will hold response detail related reservation.")
@Getter
public class ReservationResponseDto extends ResponseDto {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<FreeApartmentDto> info = new ArrayList<>();
    public ReservationResponseDto(int status, String description, String message) {
        super(status, description, message);
    }
}
