package com.example.hotels.repository.sql;

import com.example.hotels.model.sql.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IHotelJpaRepository extends JpaRepository<Hotel, Long>, JpaSpecificationExecutor<Hotel> {

    @Query("SELECT h.brand AS key, COUNT(h) AS value FROM Hotel h GROUP BY h.brand")
    List<Object[]> countGroupByBrand();

    @Query("SELECT h.address.city AS key, COUNT(h) AS value FROM Hotel h GROUP BY h.address.city")
    List<Object[]> countGroupByCity();

    @Query("SELECT h.address.country AS key, COUNT(h) AS value FROM Hotel h GROUP BY h.address.country")
    List<Object[]> countGroupByCountry();

    @Query("SELECT MAX(h.id) FROM Hotel h")
    Long findMaxId();
}