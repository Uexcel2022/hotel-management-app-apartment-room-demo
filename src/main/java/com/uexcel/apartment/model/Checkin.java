package com.uexcel.apartment.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
public class Checkin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public  Long id;
    private Integer NumberOfDays;
    private String dateIn;
    private String dateOut;
    private String name;
    private String phone;
    private double amount;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "apartment_code", referencedColumnName = "apartmentCode",foreignKey =
    @ForeignKey(name = "FK_CHECKIN_APARTMENT"))
    private Apartment apartment;
}
