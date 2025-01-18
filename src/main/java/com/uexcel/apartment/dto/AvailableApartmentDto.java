package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Schema(name ="AvailableApartment",description = "This Schema will hold available room on a given date.")
@Getter @Setter
@AllArgsConstructor
public class AvailableApartmentDto {
    private LocalDate date;
    private String apartmentCode;
}
