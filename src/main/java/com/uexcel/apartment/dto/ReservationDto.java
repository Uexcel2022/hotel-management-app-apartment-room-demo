package com.uexcel.apartment.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Schema(name ="Reservation",description = "This Schema will hold details for reservation.")
@Data
public class ReservationDto {
    @NotNull(message = "Name is required.")
    @Pattern(regexp = "[a-zA-Z]{3,} [a-zA-Z]{3,}",message = "Expecting 2 names and not less than 3 letters alone.")
    private String name;
    @NotNull(message = "Name is required.")
    @Pattern(regexp = "0[7-9][01][0-9]{8}", message = "Invalid phone number.")
    private String phone;
    @Valid
    private List<DateApartmentsDto> apartments;
}
