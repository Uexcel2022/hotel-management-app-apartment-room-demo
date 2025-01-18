package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
@Schema(name ="CheckinResponse",description = "This Schema will hold checkin details.")
@Getter @Setter
public class CheckinResponseDto {
    private Long id;
    private  String apartmentCode;
    private String dateIn;
    private String dateOut;
    private Integer numberOfDays;
    private String name;
    private String phone;
    private double amount;
}
