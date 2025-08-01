package com.example.hotels.repository.base;

import com.example.hotels.dto.HotelDetailDTO;
import com.example.hotels.dto.HotelSummaryDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IHotelBase {

    HotelDetailDTO save(HotelDetailDTO dto);

    Optional<HotelDetailDTO> findById(Long id);

    List<HotelSummaryDTO> findAll();

    List<HotelSummaryDTO> search(String name, String brand, String city, String country, List<String> amenities);

    boolean addAmenitiesToHotel(Long hotelId, List<String> amenities);

    Map<String, Long> getHistogram(String param);
}