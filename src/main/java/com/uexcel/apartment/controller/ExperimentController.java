package com.uexcel.apartment.controller;

import com.uexcel.apartment.dto.AvailableApartmentDto;
import com.uexcel.apartment.dto.ErrorResponseDto;
import com.uexcel.apartment.service.IExecutiveRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
@Tag(name = "REST APIs For Apartment  Reservation.",
        description = "The REST APIs For Apartment  Reservation CRUD operations.")
@RestController
@RequestMapping("/api/free")
@AllArgsConstructor
public class ExperimentController {
    private final IExecutiveRoomService regularRoomService;

    @Operation(summary = "The API to Search For Apartment Free Dates in Days.",
            description = "The API to Search For Apartment Free Days Can Be Filtered by Desired number of days.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",description = "Ok",
                            content = @Content(schema=@Schema(implementation= AvailableApartmentDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",description = "Bad Request",
                            content = @Content(schema=@Schema(implementation= ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",description = "Not Found",
                            content = @Content(schema=@Schema(implementation=ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",description = "Internal Server Error",
                            content = @Content(schema=@Schema(implementation= ErrorResponseDto.class))
                    )
            }

    )
    @GetMapping("/room/num-of-days")
    public ResponseEntity<Map<String,List<AvailableApartmentDto>>> getFreeRoomsByDays(
            @RequestParam(required = false) Integer numberOfDays) {
        Map<String,List<AvailableApartmentDto>> sortedResult  =
                regularRoomService.getFreeRoomsByDays(numberOfDays);
        return new ResponseEntity<>(sortedResult, HttpStatus.OK);
    }

    @Operation(summary = "The API to Search For Apartment Free Dates in Month.",
            description = "The API to Search For Apartment Free Days Can Be Filtered by Desired Month.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",description = "Ok",
                            content = @Content(schema=@Schema(implementation= AvailableApartmentDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",description = "Bad Request",
                            content = @Content(schema=@Schema(implementation= ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",description = "Not Found",
                            content = @Content(schema=@Schema(implementation=ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",description = "Internal Server Error",
                            content = @Content(schema=@Schema(implementation= ErrorResponseDto.class))
                    )
            }

    )

    @GetMapping("/room/month")
    public ResponseEntity<Map<String,List<AvailableApartmentDto>>> getFreeRoomsByMonth(
            @RequestParam(required = false) String monthName) {
        Map<String,List<AvailableApartmentDto>> sortedResult =
                regularRoomService.getFreeRoomsByMonth(monthName);
        return new ResponseEntity<>(sortedResult, HttpStatus.OK);
    }

}
