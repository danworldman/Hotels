package com.example.hotels.mapper;

import com.example.hotels.dto.*;
import com.example.hotels.model.mongo.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HotelMongoMapper {

    public static HotelDetailDTO documentToDetailDTO(HotelDocument hotel) {
        if (hotel == null) return null;

        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setId(hotel.getLongId());
        dto.setName(hotel.getName());
        dto.setBrand(hotel.getBrand());
        dto.setDescription(hotel.getDescription());

        if (hotel.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            AddressMongo addr = hotel.getAddress();
            addressDTO.setHouseNumber(addr.getHouseNumber());
            addressDTO.setStreet(addr.getStreet());
            addressDTO.setCity(addr.getCity());
            addressDTO.setCountry(addr.getCountry());
            addressDTO.setPostCode(addr.getPostCode());
            dto.setAddress(addressDTO);
        }

        if (hotel.getContacts() != null) {
            ContactsDTO contactsDTO = new ContactsDTO();
            ContactsMongo cont = hotel.getContacts();
            contactsDTO.setPhone(cont.getPhone());
            contactsDTO.setEmail(cont.getEmail());
            dto.setContacts(contactsDTO);
        }

        if (hotel.getArrivalTime() != null) {
            ArrivalTimeDTO arrivalTimeDTO = new ArrivalTimeDTO();
            ArrivalTimeMongo arrival = hotel.getArrivalTime();
            arrivalTimeDTO.setCheckIn(arrival.getCheckIn());
            arrivalTimeDTO.setCheckOut(arrival.getCheckOut());
            dto.setArrivalTime(arrivalTimeDTO);
        }

        if (hotel.getAmenities() != null) {
            List<String> amenities = hotel.getAmenities().stream()
                    .sorted()
                    .collect(Collectors.toList());
            dto.setAmenities(amenities);
        } else {
            dto.setAmenities(new ArrayList<>());
        }

        return dto;
    }

    public static HotelDocument dtoToDocument(HotelDetailDTO dto) {
        if (dto == null) return null;

        HotelDocument hotel = new HotelDocument();

        hotel.setId(null);
        hotel.setLongId(dto.getId());

        hotel.setName(dto.getName());
        hotel.setBrand(dto.getBrand());
        hotel.setDescription(dto.getDescription());

        if (dto.getAddress() != null) {
            AddressMongo addr = new AddressMongo();
            AddressDTO addrDto = dto.getAddress();
            addr.setHouseNumber(addrDto.getHouseNumber());
            addr.setStreet(addrDto.getStreet());
            addr.setCity(addrDto.getCity());
            addr.setCountry(addrDto.getCountry());
            addr.setPostCode(addrDto.getPostCode());
            hotel.setAddress(addr);
        }

        if (dto.getContacts() != null) {
            ContactsMongo contacts = new ContactsMongo();
            ContactsDTO contDto = dto.getContacts();
            contacts.setPhone(contDto.getPhone());
            contacts.setEmail(contDto.getEmail());
            hotel.setContacts(contacts);
        }

        if (dto.getArrivalTime() != null) {
            ArrivalTimeMongo arrival = new ArrivalTimeMongo();
            ArrivalTimeDTO arrivalDto = dto.getArrivalTime();
            arrival.setCheckIn(arrivalDto.getCheckIn());
            arrival.setCheckOut(arrivalDto.getCheckOut());
            hotel.setArrivalTime(arrival);
        }
        hotel.setAmenities(dto.getAmenities() != null ? dto.getAmenities() : new ArrayList<>());

        return hotel;
    }


    public static HotelSummaryDTO documentToSummaryDto(HotelDocument hotel) {
        if (hotel == null) return null;

        HotelSummaryDTO dto = new HotelSummaryDTO();
        dto.setId(hotel.getLongId());
        dto.setName(hotel.getName());
        dto.setDescription(hotel.getDescription());

        if (hotel.getAddress() != null) {
            AddressMongo addr = hotel.getAddress();
            String addressStr = (addr.getHouseNumber() != null ? addr.getHouseNumber() + " " : "") +
                    (addr.getStreet() != null ? addr.getStreet() : "") + ", " +
                    (addr.getCity() != null ? addr.getCity() : "") + ", " +
                    (addr.getPostCode() != null ? addr.getPostCode() : "") + ", " +
                    (addr.getCountry() != null ? addr.getCountry() : "");
            dto.setAddress(addressStr);
        } else {
            dto.setAddress("");
        }

        if (hotel.getContacts() != null) {
            dto.setPhone(hotel.getContacts().getPhone());
        } else {
            dto.setPhone("");
        }

        return dto;
    }

    public static HotelSummaryDTO detailDTOToSummaryDto(HotelDetailDTO detail) {
        if (detail == null) return null;
        HotelSummaryDTO dto = new HotelSummaryDTO();
        dto.setId(detail.getId());
        dto.setName(detail.getName());
        dto.setDescription(detail.getDescription());

        if (detail.getAddress() != null) {
            String addressStr = "";
            if (detail.getAddress().getHouseNumber() != null) {
                addressStr += detail.getAddress().getHouseNumber() + " ";
            }
            if (detail.getAddress().getStreet() != null) {
                addressStr += detail.getAddress().getStreet();
            }
            if (!addressStr.isEmpty()) addressStr += ", ";
            if (detail.getAddress().getCity() != null) {
                addressStr += detail.getAddress().getCity() + ", ";
            }
            if (detail.getAddress().getPostCode() != null) {
                addressStr += detail.getAddress().getPostCode() + ", ";
            }
            if (detail.getAddress().getCountry() != null) {
                addressStr += detail.getAddress().getCountry();
            }
            dto.setAddress(addressStr);
        } else {
            dto.setAddress("");
        }

        dto.setPhone(detail.getContacts() != null ? detail.getContacts().getPhone() : "");

        return dto;
    }

}