package com.example.hotels.repository.mongo;

import com.example.hotels.repository.base.IAmenityBase;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("mongodb")
public class AmenityMongoRepositoryImpl implements IAmenityBase {

    @Override
    public Optional<String> findByNameIgnoreCase(String name) {
        return Optional.ofNullable(name);
    }

    @Override
    public boolean addAmenities(List<String> amenityNames) {
        return true;
    }

    @Override
    public List<Object[]> countGroupByAmenity() {
        return List.of();
    }
}