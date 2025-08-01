package com.example.hotels.model.sql;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "hotels")
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "CLOB")
    private String description;

    private String brand;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Address address;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Contact contact;

    @OneToOne(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private ArrivalTime arrivalTime;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "hotel_amenities",
            joinColumns = @JoinColumn(name = "hotel_id"),
            inverseJoinColumns = @JoinColumn(name = "amenity_id")
    )
    private Set<Amenity> amenities = new HashSet<>();

    public Hotel() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (this.address != null) {
            this.address.setHotel(null);
        }
        this.address = address;
        if (address != null) {
            address.setHotel(this);
        }
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        if (this.contact != null) {
            this.contact.setHotel(null);
        }
        this.contact = contact;
        if (contact != null) {
            contact.setHotel(this);
        }
    }

    public ArrivalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ArrivalTime arrivalTime) {
        if (this.arrivalTime != null) {
            this.arrivalTime.setHotel(null);
        }
        this.arrivalTime = arrivalTime;
        if (arrivalTime != null) {
            arrivalTime.setHotel(this);
        }
    }

    public Set<Amenity> getAmenities() {
        return amenities;
    }

    public void setAmenities(Set<Amenity> amenities) {
        this.amenities = amenities;
    }

    public void addAmenity(Amenity amenity) {
        this.amenities.add(amenity);
        if (!amenity.getHotels().contains(this)) {
            amenity.getHotels().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hotel)) return false;
        Hotel hotel = (Hotel) o;
        return id != null && id.equals(hotel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}