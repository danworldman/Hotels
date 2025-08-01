package com.example.hotels.mapper;

import com.example.hotels.dto.*;
import com.example.hotels.model.sql.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HotelSqlMapper {

    public static HotelSummaryDTO toSummaryDTO(Hotel hotel) {
        HotelSummaryDTO dto = new HotelSummaryDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());

        if (hotel.getAddress() != null) {
            Address addr = hotel.getAddress();
            String addressStr = (addr.getHouseNumber() != null ? addr.getHouseNumber() + " " : "") +
                    (addr.getStreet() != null ? addr.getStreet() : "") + ", " +
                    (addr.getCity() != null ? addr.getCity() : "") + ", " +
                    (addr.getPostCode() != null ? addr.getPostCode() : "") + ", " +
                    (addr.getCountry() != null ? addr.getCountry() : "");
            dto.setAddress(addressStr);
        }

        if (hotel.getContact() != null) {
            dto.setPhone(hotel.getContact().getPhone());
        }

        return dto;
    }

    public static HotelDetailDTO toDetailDTO(Hotel hotel) {
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setId(hotel.getId());
        dto.setName(hotel.getName());
        dto.setBrand(hotel.getBrand());
        dto.setDescription(hotel.getDescription());

        if (hotel.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            Address addr = hotel.getAddress();
            addressDTO.setHouseNumber(addr.getHouseNumber());
            addressDTO.setStreet(addr.getStreet());
            addressDTO.setCity(addr.getCity());
            addressDTO.setCountry(addr.getCountry());
            addressDTO.setPostCode(addr.getPostCode());
            dto.setAddress(addressDTO);
        }

        if (hotel.getContact() != null) {
            ContactsDTO contactsDTO = new ContactsDTO();
            contactsDTO.setPhone(hotel.getContact().getPhone());
            contactsDTO.setEmail(hotel.getContact().getEmail());
            dto.setContacts(contactsDTO);
        }

        if (hotel.getArrivalTime() != null) {
            ArrivalTimeDTO arrivalTimeDTO = new ArrivalTimeDTO();
            if (hotel.getArrivalTime().getArrivalFrom() != null)
                arrivalTimeDTO.setCheckIn(hotel.getArrivalTime().getArrivalFrom().toString());
            if (hotel.getArrivalTime().getArrivalTo() != null)
                arrivalTimeDTO.setCheckOut(hotel.getArrivalTime().getArrivalTo().toString());
            dto.setArrivalTime(arrivalTimeDTO);
        }

        List<String> amenities = hotel.getAmenities() == null ?
                List.of() :
                hotel.getAmenities().stream()
                        .filter(a -> a != null && a.getName() != null)
                        .map(Amenity::getName)
                        .sorted(Comparator.nullsLast(String::compareTo))
                        .collect(Collectors.toList());
        dto.setAmenities(amenities);

        return dto;
    }

    public static Hotel toEntity(HotelDetailDTO dto) {
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setBrand(dto.getBrand());
        hotel.setDescription(dto.getDescription());

        if (dto.getAddress() != null) {
            Address addr = new Address();
            addr.setHouseNumber(dto.getAddress().getHouseNumber());
            addr.setStreet(dto.getAddress().getStreet());
            addr.setCity(dto.getAddress().getCity());
            addr.setCountry(dto.getAddress().getCountry());
            addr.setPostCode(dto.getAddress().getPostCode());
            hotel.setAddress(addr);
            addr.setHotel(hotel);
        }

        if (dto.getContacts() != null) {
            Contact contact = new Contact();
            contact.setPhone(dto.getContacts().getPhone());
            contact.setEmail(dto.getContacts().getEmail());
            hotel.setContact(contact);
            contact.setHotel(hotel);
        }

        if (dto.getArrivalTime() != null) {
            ArrivalTime arrivalTime = new ArrivalTime();
            try {
                if (dto.getArrivalTime().getCheckIn() != null) {
                    arrivalTime.setArrivalFrom(java.time.LocalTime.parse(dto.getArrivalTime().getCheckIn()));
                }
                if (dto.getArrivalTime().getCheckOut() != null) {
                    arrivalTime.setArrivalTo(java.time.LocalTime.parse(dto.getArrivalTime().getCheckOut()));
                }
            } catch (Exception e) {
                throw new RuntimeException("Invalid time format in ArrivalTime", e);
            }
            hotel.setArrivalTime(arrivalTime);
            arrivalTime.setHotel(hotel);
        }

        return hotel;
    }
}