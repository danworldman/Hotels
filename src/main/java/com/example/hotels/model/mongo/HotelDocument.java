package com.example.hotels.model.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "hotels")
public class HotelDocument {

    @Id
    private String id;

    private Long longId;

    private String name;
    private String description;
    private String brand;

    private AddressMongo address;
    private ContactsMongo contacts;
    private ArrivalTimeMongo arrivalTime;
    private List<String> amenities;

    public HotelDocument() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getLongId() { return longId; }
    public void setLongId(Long longId) { this.longId = longId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public AddressMongo getAddress() { return address; }
    public void setAddress(AddressMongo address) { this.address = address; }

    public ContactsMongo getContacts() { return contacts; }
    public void setContacts(ContactsMongo contacts) { this.contacts = contacts; }

    public ArrivalTimeMongo getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(ArrivalTimeMongo arrivalTime) { this.arrivalTime = arrivalTime; }

    public List<String> getAmenities() { return amenities; }
    public void setAmenities(List<String> amenities) { this.amenities = amenities; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HotelDocument)) return false;
        HotelDocument that = (HotelDocument) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}