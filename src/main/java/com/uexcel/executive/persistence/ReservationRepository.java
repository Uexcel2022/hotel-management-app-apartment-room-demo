package com.uexcel.executive.persistence;

import com.uexcel.executive.model.Reservation;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,String> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from reservation where id = ?")
    @Override
    void deleteById(@Param("id") String id);

    Reservation findReservationByPhone(String roomNumber);
}
