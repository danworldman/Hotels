package com.example.hotels.model.sql;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "amenities")
public class Amenity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "amenities")
    private Set<Hotel> hotels = new HashSet<>();

    public Amenity() {
    }

    public Amenity(String name) {
        this.name = name;
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Set<Hotel> getHotels() { return hotels; }

    public void setHotels(Set<Hotel> hotels) { this.hotels = hotels; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Amenity)) return false;
        Amenity amenity = (Amenity) o;
        return id != null && id.equals(amenity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}