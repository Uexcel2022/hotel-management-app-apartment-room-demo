package com.uexcel.executive.controller;

import com.uexcel.executive.constants.Constants;
import com.uexcel.executive.dto.ErrorResponseDto;
import com.uexcel.executive.dto.ResponseDto;
import com.uexcel.executive.model.Reservation;
import com.uexcel.executive.model.ReservationDates;
import com.uexcel.executive.persistence.ReservationDateRepository;
import com.uexcel.executive.service.IReservationDatesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Tag(name = "REST APIs For Regular Rooms  Reservation Dates.",
        description = "The REST APIs For Regular Rooms  Reservation Dates CRUD operations.")
@RestController
@RequestMapping(value = "/api/reservation/date",produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ReservationDatesController {
    private final IReservationDatesService reservationDatesService;
    private  final ReservationDateRepository reservationDateRepository;

    @Operation(summary = "The API Delete Regular Room Reservation Dates ",
            description = "The API to Delete Regular Room Reservation date",
            responses = {
                    @ApiResponse(
                            responseCode = "200",description = "Ok",
                            content = @Content(schema=@Schema(implementation= ResponseDto.class))
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

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> DeleteReservationDates(@PathVariable("id") String id){
        reservationDatesService.deleteReservationDateById(id);
        return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(),
                Constants.OK,"Reservation deleted successfully."),HttpStatus.OK);
    }

    @Operation(summary = "The API to Fetch Regular Room Reservation Dates. ",
            description = "The API to Fetch Customer's Regular Room Reservation  by Phone Number'.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",description = "Ok",
                            content = @Content(schema=@Schema(implementation= ReservationDates.class))
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

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<List<ReservationDates>> getReservationDates(
            @PathVariable("phoneNumber") String phoneNumber ){
       List<ReservationDates> reservationDates = reservationDateRepository.findByPhoneNumber(phoneNumber);
        return new ResponseEntity<>(reservationDates,HttpStatus.OK);
    }

    @Operation(summary = "The API to Update Reservation Date. ",
            description = "The API to Update Reservation Date.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",description = "Ok",
                            content = @Content(schema=@Schema(implementation= ResponseDto.class))
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

    @PutMapping
    public ResponseEntity<ResponseDto> updateReservationDates(@RequestBody List<ReservationDates> reservationDates ){
        Reservation reservation = reservationDates.getFirst().getReservation();
        List<ReservationDates> rsvDate = new ArrayList<>();
        reservationDates.forEach(reservationDate -> {
            ReservationDates dt = new ReservationDates();
            dt.setId(reservationDate.getId());
            dt.setDate(reservationDate.getDate());
            dt.setReservation(reservation);
            dt.setApartment(reservationDate.getApartment());
            rsvDate.add(dt);
        });
        reservationDateRepository.saveAll(rsvDate);
        return new ResponseEntity<>(new ResponseDto(HttpStatus.OK.value(),
                Constants.OK,"Reservation updated successfully."),HttpStatus.OK);
    }

}
