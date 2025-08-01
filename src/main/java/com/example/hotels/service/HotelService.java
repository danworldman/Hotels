package com.example.hotels.service;

import com.example.hotels.dto.HotelDetailDTO;
import com.example.hotels.dto.HotelSummaryDTO;
import com.example.hotels.repository.base.IAmenityBase;
import com.example.hotels.repository.base.IHotelBase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class HotelService {

    private final IHotelBase hotelRepository;
    private final IAmenityBase amenityRepository;

    public HotelService(IHotelBase hotelRepository, IAmenityBase amenityRepository) {
        this.hotelRepository = hotelRepository;
        this.amenityRepository = amenityRepository;
    }

    public List<HotelSummaryDTO> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Optional<HotelDetailDTO> findById(Long id) {
        return hotelRepository.findById(id);
    }

    public List<HotelSummaryDTO> search(String name, String brand, String city, String country, List<String> amenities) {
        return hotelRepository.search(name, brand, city, country, amenities);
    }

    @Transactional
    public HotelSummaryDTO createHotel(HotelDetailDTO hotelDetailDTO) {
        HotelDetailDTO savedDetail = hotelRepository.save(hotelDetailDTO);
        return convertToSummary(savedDetail);
    }

    @Transactional
    public Optional<HotelSummaryDTO> addAmenitiesToHotel(Long hotelId, List<String> amenityNames) {
        boolean updated = hotelRepository.addAmenitiesToHotel(hotelId, amenityNames);
        if (!updated) {
            return Optional.empty();
        }
        return hotelRepository.findById(hotelId).map(this::convertToSummary);
    }

    public Map<String, Long> getHistogram(String param) {
        return hotelRepository.getHistogram(param);
    }

    private HotelSummaryDTO convertToSummary(HotelDetailDTO detailDTO) {
        HotelSummaryDTO summaryDTO = new HotelSummaryDTO();
        summaryDTO.setId(detailDTO.getId());
        summaryDTO.setName(detailDTO.getName());
        summaryDTO.setDescription(detailDTO.getDescription());

        if (detailDTO.getAddress() != null) {
            StringBuilder addr = new StringBuilder();
            if (detailDTO.getAddress().getHouseNumber() != null)
                addr.append(detailDTO.getAddress().getHouseNumber()).append(" ");
            if (detailDTO.getAddress().getStreet() != null)
                addr.append(detailDTO.getAddress().getStreet());
            if (addr.length() > 0) addr.append(", ");
            if (detailDTO.getAddress().getCity() != null)
                addr.append(detailDTO.getAddress().getCity()).append(", ");
            if (detailDTO.getAddress().getPostCode() != null)
                addr.append(detailDTO.getAddress().getPostCode()).append(", ");
            if (detailDTO.getAddress().getCountry() != null)
                addr.append(detailDTO.getAddress().getCountry());

            summaryDTO.setAddress(addr.toString());
        } else {
            summaryDTO.setAddress("");
        }

        summaryDTO.setPhone(detailDTO.getContacts() != null ? detailDTO.getContacts().getPhone() : "");
        return summaryDTO;
    }
}