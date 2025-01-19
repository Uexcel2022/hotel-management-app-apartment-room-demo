package com.uexcel.apartment.persistence;

import com.uexcel.apartment.model.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    Checkin findByApartment_ApartmentCodeAndDateOut(String roomNumber, String dateOut);
    List<Checkin> findByDateOut(String dateIn);
    boolean existsByApartment_ApartmentCodeAndDateOut(String apartmentCode, String dateOut);
}
