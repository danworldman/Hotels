package com.example.hotels.repository.sql;

import com.example.hotels.model.sql.Amenity;
import com.example.hotels.repository.base.IAmenityBase;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile({"h2", "mysql", "postgresql"})
public class AmenitySqlRepositoryImpl implements IAmenityBase {

    private final IAmenityRepository IAmenityRepository;

    public AmenitySqlRepositoryImpl(IAmenityRepository IAmenityRepository) {
        this.IAmenityRepository = IAmenityRepository;
    }

    @Override
    public Optional<String> findByNameIgnoreCase(String name) {
        return IAmenityRepository.findByNameIgnoreCase(name)
                .map(a -> a.getName());
    }

    @Override
    public boolean addAmenities(List<String> amenityNames) {
        boolean changed = false;
        for (String name : amenityNames) {
            Optional<Amenity> opt = IAmenityRepository.findByNameIgnoreCase(name);
            if (opt.isEmpty()) {
                Amenity newAmenity = new Amenity();
                newAmenity.setName(name);
                IAmenityRepository.save(newAmenity);
                changed = true;
            }
        }
        return changed;
    }

    @Override
    public List<Object[]> countGroupByAmenity() {
        return IAmenityRepository.countGroupByAmenity();
    }
}