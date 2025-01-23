package com.uexcel.apartment.service.impl;

import com.uexcel.apartment.constants.Constants;
import com.uexcel.apartment.constants.Month;
import com.uexcel.apartment.dto.*;
import com.uexcel.apartment.exception.AppExceptions;
import com.uexcel.apartment.exception.ReservedRoomException;
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
import java.util.concurrent.atomic.AtomicInteger;

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
    public List<FreeApartmentDto> getFreeApartmentByMonth(String monthName,String apartmentCode) {
        if(environment.getProperty("NUMBER_OF_A1")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed,"Environment property 'NUMBER_OF_A1' not set.");
        }
        if(environment.getProperty("NUMBER_OF_A2")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed,"Environment property 'NUMBER_OF_A2' not set.");
        }

        LocalDate now = LocalDate.now();
        List<ReservationDates> reservations;
        apartmentCode = apartmentCode==null?"":apartmentCode;
        if(monthName == null){
            monthName = LocalDate.now().getMonth().toString();
        }
        int numberOfApartments;
                try {
                    if(apartmentCode.equalsIgnoreCase("a1")) {
                        numberOfApartments = Integer.parseInt(environment.getProperty("NUMBER_OF_A1"));
                        reservations =  reservationDateRepository.findByA1Apartment();
                    }else {
                        numberOfApartments = Integer.parseInt(environment.getProperty("NUMBER_OF_A2"));
                        reservations =  reservationDateRepository.findByA2Apartment();
                    }
                }catch (NumberFormatException e){
                        throw new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                                Constants.Failed, String.format("Environment property 'NUMBER_OF_%s' not integer.",apartmentCode.toUpperCase()));
                }

        List<LocalDate> reservedDates = new ArrayList<>();
        List<FreeApartmentDto> freeApartmentDtoList =  new ArrayList<>();
        List<LocalDate> monthDates = new ArrayList<>();
        for(ReservationDates reservation : reservations){
            reservedDates.add(reservation.getDate());
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

                int numberOfApartmentReserved =
                        monthDates.stream().filter(present->present.equals(date)).toList().size();
                int freeApartment = numberOfApartments - numberOfApartmentReserved;
                if(freeApartment > 0) {
                    freeApartmentDtoList.add(new FreeApartmentDto(date, freeApartment));
                }
            } else {
                if(!freeApartmentDtoList.contains(date) && (date.equals(LocalDate.now())|| date.isAfter(LocalDate.now()))){
                    freeApartmentDtoList.add(new FreeApartmentDto(date, numberOfApartments));
                }
            }
        }
        return freeApartmentDtoList;
    }

    @Override
    @Transactional
    public List<FreeApartmentDto> getFreeApartmentByDays(Integer numberOfDays,String apartmentCode) {
        if(environment.getProperty("NUMBER_OF_A2")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed,"Environment property 'NUMBER_OF_A2' not set.");
        }

        if(environment.getProperty("NUMBER_OF_A1")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed,"Environment property 'NUMBER_OF_A1' not set.");
        }
        apartmentCode = apartmentCode==null?"a1":apartmentCode;
        int numberOfApartments;
        List<ReservationDates> reservations;

        try {
            if(apartmentCode.equalsIgnoreCase("a1")) {
                numberOfApartments = Integer.parseInt(environment.getProperty("NUMBER_OF_A1"));
                reservations =  reservationDateRepository.findByA1Apartment();
            }else {
                numberOfApartments = Integer.parseInt(environment.getProperty("NUMBER_OF_A2"));
                reservations =  reservationDateRepository.findByA2Apartment();
            }
        }catch (NumberFormatException e){
                throw new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                        Constants.Failed, String.format("Environment property 'NUMBER_OF_%s' not integer.",
                        apartmentCode.toUpperCase()));
        }
        if(numberOfDays == null){
            numberOfDays = 7;
        }

        List<LocalDate> reserveDates = new ArrayList<>();
        List<FreeApartmentDto> freeApartmentDtoList =  new ArrayList<>();
        for(ReservationDates reservation : reservations){
            reserveDates.add(reservation.getDate());
        }
        reserveDates.sort(LocalDate::compareTo);

        for(int i = 0; i < numberOfDays; i++){
            LocalDate date = LocalDate.now().plusDays(i);
            if(reserveDates.contains(date) && (date.equals(LocalDate.now())|| date.isAfter(LocalDate.now()))){
                int numberOfApartmentReserved =
                        reserveDates.stream().filter(present->present.equals(date)).toList().size();
                int freeApartment = numberOfApartments - numberOfApartmentReserved;
                if(freeApartment > 0) {
                    freeApartmentDtoList.add(new FreeApartmentDto(date, freeApartment));
                }
            } else {
                if(date.equals(LocalDate.now()) || date.isAfter(LocalDate.now())){
                    freeApartmentDtoList.add(new FreeApartmentDto(date,  numberOfApartments));
                }
            }
        }
        return freeApartmentDtoList;
    }

    @Override
    @Transactional
    public ReservationResponseDto saveReservation(ReservationDto reservationDto) {
        if(environment.getProperty("NUMBER_OF_APARTMENT_A1")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed, "Environment property 'NUMBER_OF_APARTMENT_A1' not set.");
        }

        if(environment.getProperty("NUMBER_OF_APARTMENT_A2")==null){
            throw  new AppExceptions(HttpStatus.EXPECTATION_FAILED.value(),
                    Constants.Failed, "Environment property 'NUMBER_OF_APARTMENT_A1' not set.");
        }
       /*
       separation of apartment by code initials
        */
        List<DateApartmentsDto> desiredA1Apartments = new ArrayList<>();
        List<DateApartmentsDto> desiredA2Apartments = new ArrayList<>();
        List<DateApartmentsDto> listOfBookedApartment = new ArrayList<>();

        reservationDto.getApartments().forEach(apartment -> {
            for(int i = 0; i < apartment.getApartmentCodes().size();i++){
                String apartmentCode = apartment.getApartmentCodes().get(i);
                if(!apartmentRepository.existsByApartmentCode(apartmentCode)){
                    throw  new AppExceptions(HttpStatus.NOT_FOUND.value(), Constants.NotFound,
                            "No apartment found for apartment code: " + apartmentCode);
                }
                /*
                Checking for duplicate reservation for A1 apartments
                 */
                if(apartmentCode.contains("A1")){
                    DateApartmentsDto newDt1 =   new DateApartmentsDto(apartment.getDate(),List.of(apartmentCode));
                    if(!desiredA1Apartments.isEmpty()){
                        desiredA1Apartments.forEach(ap->{
                            for(DateApartmentsDto dat : desiredA1Apartments){
                                if(dat.equals(newDt1)){
                                    throw new AppExceptions(
                                            HttpStatus.BAD_REQUEST.value(), Constants.BadRequest, "Duplicate reservation for this apartment: " + apartmentCode);
                                }
                            }
                        });
                    }
                    desiredA1Apartments.add(newDt1);
                }
                /*
                Checking for duplicate reservation for A2 apartments
                 */
                if(apartmentCode.contains("A2")){
                    DateApartmentsDto  newDt2 = new DateApartmentsDto(apartment.getDate(),List.of(apartmentCode));
                    if(!desiredA2Apartments.isEmpty()){
                        desiredA2Apartments.forEach(ap->{
                            for(DateApartmentsDto dat : desiredA2Apartments){
                                if(dat.equals(newDt2)){
                                    throw new AppExceptions(
                                            HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                                            "Duplicate reservation for this apartment: " + apartmentCode);
                                }
                            }
                        });
                    }
                    desiredA2Apartments.add(newDt2);
                }
            }
        });

        /* throw  new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                    "Environment property 'NUMBER_OF_APARTMENT_A1/A2' not an integer.");
             Getting the number of the apartments respectively
         */
        int numberOfaA1;
        int numberOfaA2;
        try {
            numberOfaA1 = Integer.parseInt(environment.getProperty("NUMBER_OF_APARTMENT_A1"));
        }catch(NumberFormatException e){
            throw  new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                    "Environment property 'NUMBER_OF_APARTMENT_A2' not an integer.");
        }
        try {
            numberOfaA2 = Integer.parseInt(environment.getProperty("NUMBER_OF_APARTMENT_A2"));
        }catch(NumberFormatException e){
            throw  new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                    "Environment property 'NUMBER_OF_APARTMENT_A2' not an integer.");
        }
        /*
           Checking the available apartments vs number desired on a given date.
         */

        if(!desiredA1Apartments.isEmpty()){
            for(DateApartmentsDto dateApartments : desiredA1Apartments){
                AtomicInteger countA1 = new AtomicInteger();
                List<ReservationDates> reservedDates =
                        reservationDateRepository.findByDate(dateApartments.getDate());
                if(!reservedDates.isEmpty()) {
                    reservedDates.forEach(var -> {
                        if (var.getApartment().getApartmentCode().contains("A1")) {
                            countA1.getAndIncrement();
                        }
                    });
                }
                int desiredNumberOfA1ApartmentsForGivenDate = desiredA1Apartments.stream()
                        .filter(var->var.getDate().equals(dateApartments.getDate()))
                        .map(var->var.getApartmentCodes()).toList().size();

                if(numberOfaA1 - (desiredNumberOfA1ApartmentsForGivenDate + countA1.get()) < 0){
                    ReservationResponseDto rDto = new ReservationResponseDto(
                            HttpStatus.BAD_REQUEST.value(), "Bad Request",
                            "Available number of A1 apartments exceeded for the given date.");
                    rDto.getInfo().add(new FreeApartmentDto(dateApartments.getDate(),
                            (numberOfaA1 - countA1.get())));
                    return rDto;
                }
             }
        }

        if(!desiredA2Apartments.isEmpty()){
            for(DateApartmentsDto dateApartments : desiredA2Apartments){
                AtomicInteger countA2 = new AtomicInteger();
                List<ReservationDates> reservedDates =
                        reservationDateRepository.findByDate(dateApartments.getDate());
                if(!reservedDates.isEmpty()) {
                    reservedDates.forEach(var -> {
                        if (var.getApartment().getApartmentCode().contains("A2")) {
                            countA2.getAndIncrement();
                        }
                    });
                }
                int desiredNumberOfA2ApartmentsForGivenDate = desiredA2Apartments.stream()
                        .filter(var->var.getDate().equals(dateApartments.getDate()))
                        .map(var->var.getApartmentCodes()).toList().size();

                if(numberOfaA2 - (desiredNumberOfA2ApartmentsForGivenDate + countA2.get()) < 0){
                    ReservationResponseDto rDto = new ReservationResponseDto(
                            HttpStatus.BAD_REQUEST.value(), "Bad Request",
                            "Available number of A2 apartments exceeded for the given date.");
                    rDto.getInfo().add(new FreeApartmentDto(dateApartments.getDate(),
                            (numberOfaA2 - countA2.get())));
                    return rDto;
                }
            }
        }
        Reservation savedReservation = reservationRepository.findReservationByPhone(reservationDto.getPhone());
        if(savedReservation == null){
            Reservation  reservation = new Reservation();
            reservation.setName(reservationDto.getName());
            reservation.setPhone(reservationDto.getPhone());
            reservation.setDescription("A1Apartment");
            savedReservation  = reservationRepository.save(reservation);
        }

        /*
        A1 apartment saving logic
         */

        if(!desiredA1Apartments.isEmpty()) {
            Reservation finalSavedReservation = savedReservation;
            desiredA1Apartments.forEach(var -> {
                List<String> apartmentCode =  var.getApartmentCodes();
                apartmentCode.forEach(code -> {
                boolean isCheckin=
                        checkinRepository.existsByApartment_ApartmentCodeAndDateOut(code,null);
                if(isCheckin){
                    throw  new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                            String.format("A1 apartment '%s' is in use.", code));
                }
                Apartment res = apartmentRepository.findByApartmentCode(code);
                    if(!res.getReservationDates().isEmpty()){
                        res.getReservationDates().forEach(rsvDate -> {
                            if(rsvDate.getDate().equals(var.getDate())){
                                listOfBookedApartment.add(new DateApartmentsDto(rsvDate.getDate(),List.of(code)));
                            }
                        });
                    }
                    ReservationDates reservationDate = new ReservationDates();
                    reservationDate.setDate(var.getDate());
                    reservationDate.setApartment(res);
                    reservationDate.setReservation(finalSavedReservation);
                    reservationDateRepository.save(reservationDate);
                });
            });
        }

/*
A2 apartment saving logic
 */

        if(!desiredA2Apartments.isEmpty()) {
            Reservation finalSavedReservation = savedReservation;
            desiredA2Apartments.forEach(var -> {
                List<String> apartmentCode =  var.getApartmentCodes();
                apartmentCode.forEach(code -> {
                    boolean isCheckin=
                            checkinRepository.existsByApartment_ApartmentCodeAndDateOut(code,null);
                    if(isCheckin){
                        throw  new AppExceptions(HttpStatus.BAD_REQUEST.value(), Constants.BadRequest,
                                String.format("A2 apartment '%s' is in use.", code));
                    }
                    Apartment res = apartmentRepository.findByApartmentCode(code);
                    if(!res.getReservationDates().isEmpty()){
                        res.getReservationDates().forEach(rsvDate -> {
                            if(rsvDate.getDate().equals(var.getDate())){
                                listOfBookedApartment.add(new DateApartmentsDto(rsvDate.getDate(),List.of(code)));
                            }
                        });
                    }
                    ReservationDates reservationDate = new ReservationDates();
                    reservationDate.setDate(var.getDate());
                    reservationDate.setApartment(res);
                    reservationDate.setReservation(finalSavedReservation);
                    reservationDateRepository.save(reservationDate);
                });
            });
        }

    if(!listOfBookedApartment.isEmpty()){
        throw new ReservedRoomException(listOfBookedApartment);
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

}
