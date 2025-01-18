package com.uexcel.executive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Schema(name ="DateRooms",description = "This Schema will hold date and list of rooms.")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class DateRoomsDto {
    @FutureOrPresent(message = "Must be current or future date.")
    private LocalDate date;
    @NotEmpty(message = "Rome number can not be empty.")
    @NotNull(message = "Rome number can not be null.")
    private List<String> rooms;
}
