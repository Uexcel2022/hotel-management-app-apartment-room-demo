package com.uexcel.executive.persistence;

import com.uexcel.executive.model.ReservationDates;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationDateRepository extends JpaRepository<ReservationDates,String> {
    List<ReservationDates> findByDate(LocalDate date);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,value = "delete from reservation_dates where id = ?")
    @Override
   void deleteById(@Param("id") String id);

    boolean existsById(String id);

    @Query( value = "SELECT d FROM ReservationDates d WHERE  d.reservation.phone=:phoneNumber")
    List<ReservationDates> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    List<ReservationDates> findByExecutiveRoom_RoomNumber(String roomNumber);

}
