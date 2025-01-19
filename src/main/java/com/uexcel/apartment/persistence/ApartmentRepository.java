package com.uexcel.apartment.persistence;

import com.uexcel.apartment.model.Apartment;
import com.uexcel.apartment.model.ReservationDates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, Long> {
    @Query(value = "SELECT  d FROM  ReservationDates d WHERE d.apartment.apartmentCode=:apartmentCode ")
    List<ReservationDates> findByApartmentCodeJpql(@Param("apartmentCode") String apartmentCode);
    boolean existsByApartmentCode(String apartmentCode);
    Apartment findByApartmentCode(String apartmentCode);
}
