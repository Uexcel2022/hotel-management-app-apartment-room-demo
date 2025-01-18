package com.uexcel.executive.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Schema(name ="FreeRooms",description = "This Schema will hold number of free rooms on a given date.")
@Getter @Setter
@AllArgsConstructor
public class FreeRoomsDto {
    private LocalDate date;
    private  int numberOfRoomsAvailable;
}
