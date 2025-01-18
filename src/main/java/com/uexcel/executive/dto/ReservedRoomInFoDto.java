package com.uexcel.executive.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Schema(name ="ReservedRoomInfo",description = "This Schema will hold Room Reservation details.")
@Getter @Setter
public class ReservedRoomInFoDto {
    @NotNull
    @NotEmpty
    private Long id;
    @NotNull
    @NotEmpty
    private String roomNumber;
    @Positive
    private double price;
    private List<ReservationDetailsDto> reservationDetails = new ArrayList<>();
}
