package com.example.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Schema(description = "Детальная информация об отеле")
public class HotelDetailDTO {

    @Schema(description = "Идентификатор отеля", example = "1")
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 100, message = "Name length must be between 2 and 100 characters")
    @Schema(description = "Название отеля", example = "DoubleTree by Hilton Minsk")
    private String name;

    @Size(max = 50, message = "Brand must be at most 50 characters")
    @Schema(description = "Бренд отеля", example = "Hilton")
    private String brand;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    @Schema(description = "Описание отеля", example = "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms ...")
    private String description;

    @NotNull(message = "Address must not be null")
    @Valid
    @Schema(description = "Адрес отеля")
    private AddressDTO address;

    @NotNull(message = "Contact must not be null")
    @Valid
    @Schema(description = "Контакты отеля")
    private ContactsDTO contacts;

    @Valid
    @Schema(description = "Время заселения и выселения")
    private ArrivalTimeDTO arrivalTime;

    @Schema(description = "Список удобств (amenities)")
    @Size(min = 0)
    private List<String> amenities;

    public HotelDetailDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public ContactsDTO getContacts() {
        return contacts;
    }

    public void setContacts(ContactsDTO contacts) {
        this.contacts = contacts;
    }

    public ArrivalTimeDTO getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ArrivalTimeDTO arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }
}