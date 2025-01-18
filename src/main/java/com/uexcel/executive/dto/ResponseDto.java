package com.uexcel.executive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Schema(name ="Response",description = "This Schema will hold  response details.")
public class ResponseDto {
    private int status;
    private String description;
    private String message;
}
