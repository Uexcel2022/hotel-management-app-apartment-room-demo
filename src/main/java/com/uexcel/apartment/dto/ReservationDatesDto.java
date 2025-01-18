package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
@Schema(name ="ReservationDate",description = "This Schema will hold reservation date.")
@Getter @AllArgsConstructor
public class ReservationDatesDto {
    @NotNull
    @NotEmpty
    private String id;
    @FutureOrPresent
    private LocalDate date;
}
