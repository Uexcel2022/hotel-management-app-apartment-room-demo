package com.uexcel.apartment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
@Schema(name ="ErrorResponse",description = "This Schema will hold error details.")
@AllArgsConstructor
@Getter
public class ErrorResponseDto {
    private  String timestamp;
    private int status;
    private  String error;
    private  String message;
    private  String apiPath;
}
