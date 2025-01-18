package com.uexcel.apartment.service.impl;

import com.uexcel.apartment.constants.Constants;
import com.uexcel.apartment.constants.Month;
import com.uexcel.apartment.dto.*;
import com.uexcel.apartment.exception.AppExceptions;
import com.uexcel.apartment.exception.ReservedRoomException;
import com.uexcel.apartment.model.Checkin;
import com.uexcel.apartment.model.Apartment;
import com.uexcel.apartment.model.Reservation;
import com.uexcel.apartment.model.ReservationDates;
import com.uexcel.apartment.persistence.CheckinRepository;
import com.uexcel.apartment.persistence.ApartmentRepository;
import com.uexcel.apartment.persistence.ReservationDateRepository;
import com.uexcel.apartment.persistence.ReservationRepository;
import com.uexcel.apartment.service.IReservationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class IReservationServiceImpl implements IReservationService {
    private  final ReservationRepository reservationRepository;
    private  final ReservationDateRepository reservationDateRepository;
    private  final CheckinRepository checkinRepository;
    private final Month month;
    private final Environment environment;
    private final ApartmentRepository apartmentRepository;

    @Override
    public List<FreeApartmentDto> getFreeRoomsByMonth(String monthName) {
        if(environment.getProperty("NUMBER_OF_ROOMS")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed,"Environment property 'NUMBER_OF_ROOMS' not set.");
        }
        LocalDate now = LocalDate.now();
        if(monthName == null){
            monthName = LocalDate.now().getMonth().toString();
        }
        int numberOfRooms;
                try {
                    numberOfRooms =  Integer.parseInt(environment.getProperty("NUMBER_OF_ROOMS"));
                }catch (NumberFormatException e){
                        throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                                Constants.Failed,"Environment property 'NUMBER_OF_ROOMS' not integer.");
                }
        List<Reservation> reservations = reservationRepository.findAll();
        List<LocalDate> reservedDates = new ArrayList<>();
        List<FreeApartmentDto> freeApartmentDtoList =  new ArrayList<>();
        List<LocalDate> monthDates = new ArrayList<>();
        for(Reservation reservation : reservations){
            reservation.getReservationDates()
                    .forEach(reservedDate->reservedDates.add(reservedDate.getDate()));
        }
        reservedDates.sort(LocalDate::compareTo);
        LocalDate monthStartDate = month.getStartDate(monthName.toUpperCase());

        for(int i = 0; i < reservedDates.size(); i++){
            LocalDate monthDate = reservedDates.get(i);
            if(monthDate.getMonth().equals(monthStartDate.getMonth())){
                monthDates.add(monthDate);
            }
        }

        if(monthStartDate.equals("DECEMBER") && monthName != null && monthName.toUpperCase().equals("JANUARY")){
            monthStartDate =  LocalDate.of(now.getYear()+1,1,1);
        }

        if(monthStartDate.getDayOfYear() < now.getDayOfYear() && monthStartDate.getYear() == now.getYear()
                && ! monthStartDate.getMonth().equals(now.getMonth())){
            throw new AppExceptions(HttpStatus.BAD_REQUEST.value(),
                    "Bad Request","The desired month is not available."
            );
        }

        int mothLength = monthStartDate.lengthOfMonth();
        for(int i = 0; i < mothLength; i ++){
            LocalDate date = monthStartDate.plusDays(i);
            if(monthDates.contains(date) && !freeApartmentDtoList.contains(date) &&
                    (date.equals(LocalDate.now())|| date.isAfter(LocalDate.now()))){
                int numberOfRoomsReserved =
                        monthDates.stream().filter(present->present.equals(date)).toList().size();
                int freeRooms = numberOfRooms - numberOfRoomsReserved;
                if(freeRooms > 0) {
                    freeApartmentDtoList.add(new FreeApartmentDto(date,freeRooms));
                }
            } else {
                if(!freeApartmentDtoList.contains(date) && (date.equals(LocalDate.now())|| date.isAfter(LocalDate.now()))){
                    freeApartmentDtoList.add(new FreeApartmentDto(date,  numberOfRooms));
                }
            }
        }
        return freeApartmentDtoList;
    }

    @Override
    public List<FreeApartmentDto> getFreeRoomsByDays(Integer numberOfDays) {
        if(environment.getProperty("NUMBER_OF_ROOMS")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed, "Environment property 'NUMBER_OF_ROOMS' not set.");
        }
        int numberOfRooms;
        try {
            numberOfRooms = Integer.parseInt(environment.getProperty("NUMBER_OF_ROOMS"));
        }catch(NumberFormatException e){
            throw  new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                    "Environment property 'NUMBER_OF_ROOMS' not an integer.");
        }
        List<Reservation> reservations = reservationRepository.findAll();
        if(numberOfDays == null){
            numberOfDays = 30;
        }
        List<LocalDate> reserveDates = new ArrayList<>();
        List<FreeApartmentDto> freeApartmentDtoList =  new ArrayList<>();
        for(Reservation reservation : reservations){
            reservation.getReservationDates()
                    .forEach(bd-> reserveDates.add(bd.getDate()));
        }
        reserveDates.sort(LocalDate::compareTo);

        for(int i = 0; i < numberOfDays; i++){
            LocalDate date = LocalDate.now().plusDays(i);
            if(!freeApartmentDtoList.contains(date) && reserveDates.contains(date) &&
                    (date.equals(LocalDate.now())|| date.isAfter(LocalDate.now()))){
                int numberOfRoomsReserved =
                        reserveDates.stream().filter(present->present.equals(date)).toList().size();
                int freeRooms = numberOfRooms - numberOfRoomsReserved;
                if(freeRooms > 0) {
                    freeApartmentDtoList.add(new FreeApartmentDto(date,freeRooms));
                }
            } else {
                if((date.equals(LocalDate.now()) || date.isAfter(LocalDate.now())) && !freeApartmentDtoList.contains(date)){
                    freeApartmentDtoList.add(new FreeApartmentDto(date,  numberOfRooms));
                }
            }
        }
        return freeApartmentDtoList;
    }

    @Override
    @Transactional
    public ReservationResponseDto saveReservation(ReservationDto reservationDto) {
        if(environment.getProperty("NUMBER_OF_ROOMS")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed, "Environment property 'NUMBER_OF_ROOMS' not set.");
        }

        /*
             Getting previous booking from DB by Date. Each date represents booking per room
             The total number of date of same day represents total reserved room for the given date.
             If the total equal the total number of room then all the rooms are reserved on that date.
             Else if the total number + the intended reservations exceeds the number of room for the given date,
             or any of the rooms mentioned is reserved on same date reservation fails else it passes.
         */
        List<DateApartmentsDto> dateApartmentsDtos = new ArrayList<>();
        int numberOfRooms;
        try {
            numberOfRooms = Integer.parseInt(environment.getProperty("NUMBER_OF_ROOMS"));
        }catch(NumberFormatException e){
            throw  new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                    "Environment property 'NUMBER_OF_ROOMS' not an integer.");
        }
        List<FreeApartmentDto> unAvailableDates = new ArrayList<>();
        DateApartmentsDto booking;
        for(int i = 0; i < reservationDto.getDates().size();i++){
            booking = reservationDto.getDates().get(i);
           List<ReservationDates> reservedDates =
                   reservationDateRepository.findByDate(booking.getDate());
           if(reservedDates == null){
               continue;
           }
           int reservations = reservedDates.size();
            int intendToBeReserved = booking.getApartments().size();
           if(numberOfRooms - (reservations + intendToBeReserved) < 0){
               unAvailableDates.add(
                       new FreeApartmentDto(booking.getDate(),(numberOfRooms-reservations)));
           }
        }

        if(!unAvailableDates.isEmpty()){
            ReservationResponseDto rDto = new ReservationResponseDto(
                    HttpStatus.BAD_REQUEST.value(), "Bad Request",
                    "Available number of rooms exceeded.");
            rDto.getInfo().addAll(unAvailableDates);
            return rDto;
        }

        /*
            checking for existing reservation if found add the current reservation to it.
            (to maintain phone number unique constraint in the reservation table) else create new reservation.
         */
        Reservation savedReservation = reservationRepository
                .findReservationByPhone(reservationDto.getPhone());
        if(savedReservation == null) {
            Reservation reservation = new Reservation();
            reservation.setName(reservationDto.getName());
            reservation.setPhone(reservationDto.getPhone());
            reservation.setDescription("executive");
            savedReservation = reservationRepository.save(reservation);
            if (savedReservation.getId() == null) {
                throw new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                        Constants.Failed, "Fail to save reservation.");

            }
        }

        List<ReservationDates> reservationDates = getReservationDates(reservationDto, savedReservation, dateApartmentsDtos);

        if(!dateApartmentsDtos.isEmpty()){
            throw new ReservedRoomException(dateApartmentsDtos);
        }
      List<ReservationDates> savedResDates =  reservationDateRepository.saveAll(reservationDates);
        for(ReservationDates rs : savedResDates){
            if(rs.getId() ==null){
                throw new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                        Constants.Failed, "Fail to save reservation dates.");
            }
        }
        return new ReservationResponseDto(
                HttpStatus.CREATED.value(), "Created",
                "Reservation created successfully.");
    }

    @Override
    public ResponseDto deletePastReservations(){
            List<Reservation> reservations = reservationRepository.findAll();
            if (reservations.isEmpty()) {
                return new ResponseDto(HttpStatus.NOT_FOUND.value(),
                        "Not Found", "No reservation found");
            }
            for (Reservation r : reservations) {
                List<ReservationDates> reservationDatesDate = r.getReservationDates();
                /*
                   if reservation has n reservation date delete
                 */
                int rSize = reservationDatesDate.size();
                if(rSize==0){
                    reservationRepository.deleteById(r.getId());
                }
                for (ReservationDates resDate : reservationDatesDate) {
                    if (LocalDate.now().isAfter(resDate.getDate())) {
                        reservationDateRepository.deleteById(resDate.getId());
                            if(rSize==1){
                                reservationRepository.deleteById(r.getId());
                            }
                    }
                }
            }
        return new ResponseDto(HttpStatus.OK.value(),
                "Ok", "Reservation deleted successfully.");
    }

    private  List<ReservationDates>  getReservationDates(
            ReservationDto reservationDto, Reservation savedReservation, List<DateApartmentsDto> dateApartmentsDtos){
        List<ReservationDates> reservationDates = new ArrayList<>();
        /*
             Streaming and creating obj of the intended reserved dates from the customer
         */
        reservationDto.getDates().forEach(res -> {
            for(int i = 0; i < res.getApartments().size(); i++) {

                ReservationDates reservationDate = new ReservationDates();

                Apartment room =  apartmentRepository.findByApartmentCode(res.getApartments().get(i));
                if(room == null){
                    throw new AppExceptions(HttpStatus.NOT_FOUND.value(), Constants.NotFound,
                            String.format("No room found with roomNumber: %s", res.getApartments().get(i)));
                }
                /*
                    checking if the room is checkin without reservation for the desired dates.
                 */
                    Checkin checkin = checkinRepository
                            .findByExecutiveRoom_RoomNumberAndDateOut(room.getApartmentCode(), null);
                    if (checkin != null) {
                        int numberOfDays = checkin.getNumberOfDays();
                        String checkinDate = checkin.getDateIn().split(" ")[0];
                        LocalDate duration = LocalDate.parse(checkinDate).plusDays(numberOfDays);
                        if (!res.getDate().isAfter(duration)) {
                            throw new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                                    String.format("Room %s is not available within the period request.", room.getApartmentCode()));
                        }
                    }
                /*
                    Checking if  the room(s) is/are reserved on the desired date;
                 */
                    if (room != null && room.getReservationDates().stream().anyMatch(v -> v.getDate().equals(res.getDate()))) {
                        dateApartmentsDtos.add(new DateApartmentsDto(res.getDate(), List.of(res.getApartments().get(i))));
                    }
                    reservationDate.setDate(res.getDate());
                    reservationDate.setReservation(savedReservation);
                    reservationDate.setApartment(room);
                    reservationDates.add(reservationDate);
            }
        });

        return reservationDates;
    }
}
