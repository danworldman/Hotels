package com.example.hotels.repository.base;

import java.util.List;
import java.util.Optional;

public interface IAmenityBase {

    Optional<String> findByNameIgnoreCase(String name);

    boolean addAmenities(List<String> amenityNames);

    List<Object[]> countGroupByAmenity();
}