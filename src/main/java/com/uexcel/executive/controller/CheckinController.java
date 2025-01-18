package com.uexcel.executive.controller;

import com.uexcel.executive.dto.CheckinRequestDto;
import com.uexcel.executive.dto.CheckinResponseDto;
import com.uexcel.executive.dto.ErrorResponseDto;
import com.uexcel.executive.dto.ResponseDto;
import com.uexcel.executive.service.ICheckinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Tag(name = "REST APIs For Checkin CRUD Operations For Regular Rooms.",
        description = "The REST APIs For checkin related CRUD operations.")
@RestController
@AllArgsConstructor
@Validated
@RequestMapping(value = "/api/checkin",produces = MediaType.APPLICATION_JSON_VALUE)
public class CheckinController {
    private  final ICheckinService checkinService;
    @Operation(summary = "The API to Checkin Customer to Regular Room.",
            description = "Regular Room API for Checkin of Customer.",
            responses = {
            @ApiResponse(
                    responseCode = "201",description = "Created",
                    content = @Content(schema=@Schema(implementation=ResponseDto.class))
            ),
                    @ApiResponse(
                            responseCode = "400",description = "Bad Request",
                            content = @Content(schema=@Schema(implementation=ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "417",description = "Exception Failed",
                            content = @Content(schema=@Schema(implementation=ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",description = "Internal Server Error",
                            content = @Content(schema=@Schema(implementation= ErrorResponseDto.class))
                    )
            }

    )
    @PostMapping
    public ResponseEntity<ResponseDto> checkin(@Valid @RequestBody CheckinRequestDto checkinRequestDto) {
        ResponseDto rs = checkinService.checkin(checkinRequestDto);
        return new ResponseEntity<>(rs, HttpStatus.CREATED);
    }

    @Operation(summary = "The API to Checkout Customer to Regular Room.",
            description = "Regular Room API for Checkout of Customer.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",description = "Ok",
                            content = @Content(schema=@Schema(implementation=ResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",description = "Bad Request",
                            content = @Content(schema=@Schema(implementation=ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "417",description = "Exception Failed",
                            content = @Content(schema=@Schema(implementation=ErrorResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",description = "Internal Server Error",
                            content = @Content(schema=@Schema(implementation= ErrorResponseDto.class))
                    )
            }

    )
    @PutMapping("/{checkinId}")
    public ResponseEntity<ResponseDto> checkout(@PathVariable Long checkinId) {
        ResponseDto rs = checkinService.checkout(checkinId);
        return new ResponseEntity<>(rs, HttpStatus.OK);
    }

    @Operation(summary = "The API to get Checkin  Room checkin Information.",
            description = "Regular Room API for A Room Checkin information .",
            responses = {
                    @ApiResponse(
                            responseCode = "200",description = "Ok",
                            content = @Content(schema=@Schema(implementation=CheckinResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",description = "Bad Request",
                            content = @Content(schema=@Schema(implementation=ErrorResponseDto.class))
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

    @GetMapping
    public ResponseEntity<List<CheckinResponseDto>> getCheckins(
            @RequestParam(required = false) String roomNumber){
        List<CheckinResponseDto> result = checkinService.getCheckin(roomNumber);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
