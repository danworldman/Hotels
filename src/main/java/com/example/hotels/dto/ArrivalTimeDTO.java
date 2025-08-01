package com.example.hotels.dto;

import jakarta.validation.constraints.Pattern;

public class ArrivalTimeDTO {
    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "Check-in time must be in HH:mm format")
    private String checkIn;

    @Pattern(regexp = "^([01]\\d|2[0-3]):[0-5]\\d$", message = "Check-out time must be in HH:mm format")
    private String checkOut;

    public ArrivalTimeDTO() {
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}