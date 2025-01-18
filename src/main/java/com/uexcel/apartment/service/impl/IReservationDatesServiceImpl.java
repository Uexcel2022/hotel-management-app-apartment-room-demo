package com.uexcel.apartment.service.impl;

import com.uexcel.apartment.constants.Constants;
import com.uexcel.apartment.exception.AppExceptions;
import com.uexcel.apartment.model.ReservationDates;
import com.uexcel.apartment.persistence.ReservationDateRepository;
import com.uexcel.apartment.persistence.ReservationRepository;
import com.uexcel.apartment.service.IReservationDatesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class IReservationDatesServiceImpl implements IReservationDatesService {
    private final ReservationDateRepository reservationDateRepository;
    private final ReservationRepository reservationRepository;
    @Override
    public void deleteReservationDateById(String reservationDateId) {
        ReservationDates reservationDates =
                reservationDateRepository.findById(reservationDateId)
                .orElseThrow(()-> new AppExceptions(HttpStatus.NOT_FOUND.value(),
                        Constants.NotFound, "No reservation found for Id: "+reservationDateId));
        reservationDates.getReservation().getReservationDates().remove(reservationDates);

        if(reservationDates.getReservation().getReservationDates().isEmpty()){
            reservationRepository.deleteById(reservationDates.getReservation().getId());
        }else {
            reservationDateRepository.deleteById(reservationDates.getId());
        }
    }

}
