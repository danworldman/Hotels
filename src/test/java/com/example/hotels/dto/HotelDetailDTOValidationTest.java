package com.example.hotels.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class HotelDetailDTOValidationTest {

    private static Validator validator;

    @BeforeAll
    public static void setupValidatorInstance() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidHotelDetailDTO_NoViolations() {
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setName("Valid Hotel");
        dto.setBrand("BrandName");
        dto.setDescription("Nice hotel");
        dto.setAmenities(List.of("Free WiFi"));

        AddressDTO address = new AddressDTO();
        address.setHouseNumber(10);
        address.setStreet("Street");
        address.setCity("City");
        address.setCountry("Country");
        address.setPostCode("12345");
        dto.setAddress(address);

        ContactsDTO contacts = new ContactsDTO();
        contacts.setPhone("+123456789");
        contacts.setEmail("test@example.com");
        dto.setContacts(contacts);

        ArrivalTimeDTO arrivalTime = new ArrivalTimeDTO();
        arrivalTime.setCheckIn("14:00");
        arrivalTime.setCheckOut("12:00");
        dto.setArrivalTime(arrivalTime);

        Set<ConstraintViolation<HotelDetailDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty(), "Expected no validation errors");
    }

    @Test
    public void testNameIsBlank_ShouldFail() {
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setName("");
        dto.setAddress(new AddressDTO());
        dto.setContacts(new ContactsDTO());
        dto.setAmenities(List.of("Free WiFi"));

        Set<ConstraintViolation<HotelDetailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name") && v.getMessage().contains("mandatory")));
    }

    @Test
    public void testAddressIsNull_ShouldFail() {
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setName("Hotel");
        dto.setAddress(null);
        dto.setContacts(new ContactsDTO());
        dto.setAmenities(List.of("Free WiFi"));

        Set<ConstraintViolation<HotelDetailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("address") && v.getMessage().contains("not be null")));
    }

    @Test
    public void testContactsIsNull_ShouldFail() {
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setName("Hotel");
        dto.setAddress(new AddressDTO());
        dto.setContacts(null);
        dto.setAmenities(List.of("Free WiFi"));

        Set<ConstraintViolation<HotelDetailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("contacts") && v.getMessage().contains("not be null")));
    }

    @Test
    public void testArrivalTimeInvalidFormat_ShouldFail() {
        ArrivalTimeDTO arrivalTimeDTO = new ArrivalTimeDTO();
        arrivalTimeDTO.setCheckIn("25:00");  // Неправильный час
        arrivalTimeDTO.setCheckOut("99:99"); // Неправильное время

        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setName("Hotel");
        dto.setAddress(new AddressDTO());
        dto.setContacts(new ContactsDTO());
        dto.setAmenities(List.of("Free WiFi"));
        dto.setArrivalTime(arrivalTimeDTO);

        Set<ConstraintViolation<HotelDetailDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().contains("arrivalTime.checkIn")));
    }
}