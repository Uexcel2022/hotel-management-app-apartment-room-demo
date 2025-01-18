package com.uexcel.executive.persistence;

import com.uexcel.executive.model.Checkin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckinRepository extends JpaRepository<Checkin, Long> {
    Checkin findByExecutiveRoom_RoomNumberAndDateOut(String roomNumber, String dateOut);
    List<Checkin> findByDateOut(String dateIn);
    boolean existsByExecutiveRoom_RoomNumberAndDateOut(String roomNumber, String dateOut);
}
