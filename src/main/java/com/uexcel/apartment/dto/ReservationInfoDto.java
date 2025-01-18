package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Schema(name ="Reservation",description = "This Schema will hold customer details and reservation")
@Getter @AllArgsConstructor
public class ReservationInfoDto {
    private String id;
    private String name;
    private String phone;
    private String description;
}
