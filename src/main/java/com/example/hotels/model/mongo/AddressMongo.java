package com.example.hotels.model.mongo;

import java.util.Objects;

public class AddressMongo {

    private Integer houseNumber;
    private String street;
    private String city;
    private String country;
    private String postCode;

    public AddressMongo() {
    }

    public AddressMongo(Integer houseNumber, String street, String city, String country, String postCode) {
        this.houseNumber = houseNumber;
        this.street = street;
        this.city = city;
        this.country = country;
        this.postCode = postCode;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(Integer houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AddressMongo)) return false;
        AddressMongo that = (AddressMongo) o;
        return Objects.equals(houseNumber, that.houseNumber) &&
                Objects.equals(street, that.street) &&
                Objects.equals(city, that.city) &&
                Objects.equals(country, that.country) &&
                Objects.equals(postCode, that.postCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(houseNumber, street, city, country, postCode);
    }

    @Override
    public String toString() {
        return "AddressMongo{" +
                "houseNumber=" + houseNumber +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", postCode='" + postCode + '\'' +
                '}';
    }
}