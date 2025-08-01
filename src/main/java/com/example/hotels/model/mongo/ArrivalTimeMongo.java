package com.example.hotels.model.mongo;

import java.util.Objects;

public class ArrivalTimeMongo {

    private String checkIn;
    private String checkOut;

    public ArrivalTimeMongo() {
    }

    public ArrivalTimeMongo(String checkIn, String checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArrivalTimeMongo)) return false;
        ArrivalTimeMongo that = (ArrivalTimeMongo) o;
        return Objects.equals(checkIn, that.checkIn) &&
                Objects.equals(checkOut, that.checkOut);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkIn, checkOut);
    }

    @Override
    public String toString() {
        return "ArrivalTimeMongo{" +
                "checkIn='" + checkIn + '\'' +
                ", checkOut='" + checkOut + '\'' +
                '}';
    }
}
