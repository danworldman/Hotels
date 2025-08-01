package com.example.hotels.model.sql;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "arrival_times")
public class ArrivalTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "check_in")
    private LocalTime arrivalFrom;

    @Column(name = "check_out")
    private LocalTime arrivalTo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;

    public ArrivalTime() {
    }

    public Long getId() {
        return id;
    }

    public LocalTime getArrivalFrom() {
        return arrivalFrom;
    }

    public void setArrivalFrom(LocalTime arrivalFrom) {
        this.arrivalFrom = arrivalFrom;
    }

    public LocalTime getArrivalTo() {
        return arrivalTo;
    }

    public void setArrivalTo(LocalTime arrivalTo) {
        this.arrivalTo = arrivalTo;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrivalTime)) return false;
        ArrivalTime that = (ArrivalTime) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}