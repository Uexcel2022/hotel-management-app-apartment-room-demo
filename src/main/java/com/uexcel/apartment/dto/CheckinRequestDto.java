package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
@Schema(name ="CheckinRequest",description = "This Schema will hold info for checking in customer.")
@Getter @Setter
public class CheckinRequestDto {
    @NotNull(message = "Room number is required.")
    @Pattern(regexp = "(A1|A2)[0-9]{3}",message = "In valid room number.")
    private  String apartmentCode;
    @Positive(message = "Number of days must be greater than zero.")
    @NotNull(message = "Number of days can not be null.")
    private Integer numberOfDays;
    @NotNull(message = "Name is required.")
    @Pattern(regexp = "[a-zA-Z]{3,} [a-zA-Z]{3,}",message = "Expecting 2  names and not less than 3 letters.")
    private String name;
    @NotNull(message = "Name is required.")
    @Pattern(regexp = "0[7-9][01][0-9]{8}", message = "Invalid phone number.")
    private String phone;
}
