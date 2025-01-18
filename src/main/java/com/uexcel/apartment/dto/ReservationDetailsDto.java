package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Schema(name ="ReservationDetails",description = "This Schema will hold reservation dates and customer info.")
@Getter @AllArgsConstructor
public class ReservationDetailsDto {
    private ReservationDatesDto reservedDate;
    private ReservationInfoDto reservation;
}
