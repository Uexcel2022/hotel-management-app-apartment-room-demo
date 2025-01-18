package com.uexcel.apartment.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
@Entity
@Getter
@Setter
@ToString
public class ReservationDates {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate date;
    @ManyToOne(optional = false,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Reservation reservation;
    @ManyToOne(optional = false,cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "roomNumber",referencedColumnName = "roomNumber",
            foreignKey = @ForeignKey(name = "FK_RD_REGULAR"))
    private Apartment apartment;
}
