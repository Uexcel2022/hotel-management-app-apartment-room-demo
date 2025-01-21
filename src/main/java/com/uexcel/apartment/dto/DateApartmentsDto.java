package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
@Schema(name ="DateApartments",description = "This Schema will hold date and list of rooms.")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateApartmentsDto {
    @FutureOrPresent(message = "Must be current or future date.")
    private LocalDate date;
    @NotEmpty(message = "Apartment Code can not be empty.")
    @NotNull(message = "Apartment Code can not be null.")
    private List<String> apartmentCodes;
}
