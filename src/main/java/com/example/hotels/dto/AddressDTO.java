package com.example.hotels.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AddressDTO {
    @NotNull(message = "House number must not be null")
    private Integer houseNumber;

    @NotBlank(message = "Street must not be blank")
    @Size(max = 100, message = "Street must be at most 100 characters")
    private String street;

    @NotBlank(message = "City must not be blank")
    @Size(max = 50, message = "City must be at most 50 characters")
    private String city;

    @NotBlank(message = "Country must not be blank")
    @Size(max = 50, message = "Country must be at most 50 characters")
    private String country;

    @NotBlank(message = "Post code must not be blank")
    @Size(max = 20, message = "Post code must be at most 20 characters")
    private String postCode;

    public AddressDTO() {
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
}