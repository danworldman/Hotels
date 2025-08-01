package com.example.hotels.repository.sql;

import com.example.hotels.model.sql.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAmenityRepository extends JpaRepository<Amenity, Long> {

    Optional<Amenity> findByNameIgnoreCase(String name);

    @Query("SELECT a.name AS key, COUNT(h) AS value FROM Amenity a JOIN a.hotels h GROUP BY a.name")
    List<Object[]> countGroupByAmenity();
}