package com.example.hotels.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.hotels.HotelsApplication;
import com.example.hotels.model.sql.Address;
import com.example.hotels.model.sql.ArrivalTime;
import com.example.hotels.model.sql.Contact;
import com.example.hotels.model.sql.Hotel;
import com.example.hotels.repository.sql.IHotelJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = HotelsApplication.class)
@AutoConfigureMockMvc
public class HotelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IHotelJpaRepository hotelJpaRepository;

    private Long hotelId;

    @BeforeEach
    public void setup() {
        hotelJpaRepository.deleteAll(); // Очистить перед каждым тестом

        Hotel hotel = new Hotel();
        hotel.setName("DoubleTree by Hilton Minsk");
        hotel.setBrand("Hilton");
        hotel.setDescription("The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms ...");

        Address address = new Address();
        address.setHouseNumber(9);
        address.setStreet("Pobediteley Avenue");
        address.setCity("Minsk");
        address.setCountry("Belarus");
        address.setPostCode("220004");
        hotel.setAddress(address);
        address.setHotel(hotel);

        Contact contact = new Contact();
        contact.setPhone("+375 17 309-80-00");
        contact.setEmail("doubletreeminsk.info@hilton.com");
        hotel.setContact(contact);
        contact.setHotel(hotel);

        ArrivalTime arrivalTime = new ArrivalTime();
        arrivalTime.setArrivalFrom(java.time.LocalTime.parse("14:00"));
        arrivalTime.setArrivalTo(java.time.LocalTime.parse("12:00"));
        hotel.setArrivalTime(arrivalTime);
        arrivalTime.setHotel(hotel);

        Hotel savedHotel = hotelJpaRepository.save(hotel);
        this.hotelId = savedHotel.getId();
    }

    @Test
    public void testGetHotelById_Valid() throws Exception {
        mockMvc.perform(get("/property-view/hotels/" + hotelId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.description").isString())
                .andExpect(jsonPath("$.brand").isString())
                .andExpect(jsonPath("$.address.houseNumber").isNumber())
                .andExpect(jsonPath("$.address.street").isString())
                .andExpect(jsonPath("$.address.city").isString())
                .andExpect(jsonPath("$.address.country").isString())
                .andExpect(jsonPath("$.address.postCode").isString())
                .andExpect(jsonPath("$.contacts.phone").isString())
                .andExpect(jsonPath("$.contacts.email").isString())
                .andExpect(jsonPath("$.arrivalTime.checkIn").isString())
                .andExpect(jsonPath("$.arrivalTime.checkOut").isString())
                .andExpect(jsonPath("$.amenities").isArray());
    }

    @Test
    public void testGetHotelById_NotFound() throws Exception {
        long nonExistingId = 99999L;
        mockMvc.perform(get("/property-view/hotels/" + nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Hotel with id " + nonExistingId + " not found"));
    }

    @Test
    public void testGetHotels_ReturnsList() throws Exception {
        mockMvc.perform(get("/property-view/hotels")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                // Проверяем, что есть хотя бы один отель с нужными полями
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].description").isString())
                .andExpect(jsonPath("$[0].address").isString())
                .andExpect(jsonPath("$[0].phone").isString());
    }

    @Test
    public void testCreateHotel_ValidRequest() throws Exception {
        String newHotelJson = """
        {
          "name": "Test Hotel",
          "description": "Description of Test Hotel",
          "brand": "TestBrand",
          "address": {
            "houseNumber": 123,
            "street": "Test Street",
            "city": "TestCity",
            "country": "TestCountry",
            "postCode": "12345"
          },
          "contacts": {
            "phone": "+1234567890",
            "email": "test@example.com"
          },
          "arrivalTime": {
            "checkIn": "14:00",
            "checkOut": "12:00"
          },
          "amenities": ["Free WiFi"]
        }
        """;

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newHotelJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Test Hotel"))
                .andExpect(jsonPath("$.description").value("Description of Test Hotel"))

                // <-- Вместо jsonPath с вложенными полями адреса:
                .andExpect(jsonPath("$.address").isString())
                .andExpect(jsonPath("$.address").value(org.hamcrest.Matchers.containsString("Test Street")))

                .andExpect(jsonPath("$.phone").isString());
    }


    @Test
    public void testCreateHotel_MissingName_ShouldReturnBadRequest() throws Exception {
        String invalidHotelJson = """
            {
              "description": "Description of Test Hotel",
              "brand": "TestBrand",
              "address": {
                "houseNumber": 123,
                "street": "Test Street",
                "city": "TestCity",
                "country": "TestCountry",
                "postCode": "12345"
              },
              "contacts": {
                "phone": "+1234567890",
                "email": "test@example.com"
              },
              "arrivalTime": {
                "checkIn": "14:00",
                "checkOut": "12:00"
              }
            }
            """;

        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidHotelJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void testAddAmenities_Valid() throws Exception {
        String amenitiesJson = """
            [
              "Free parking",
              "Free WiFi",
              "Non-smoking rooms"
            ]
            """;

        mockMvc.perform(post("/property-view/hotels/" + hotelId + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenitiesJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void testAddAmenities_NonExistingHotel_ShouldReturnNotFound() throws Exception {
        String amenitiesJson = """
            [
              "Free parking"
            ]
            """;

        mockMvc.perform(post("/property-view/hotels/99999/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenitiesJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void testAddAmenities_EmptyList_ShouldReturnBadRequest() throws Exception {
        String emptyAmenitiesJson = "[]";

        mockMvc.perform(post("/property-view/hotels/" + hotelId + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyAmenitiesJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void testAddAmenities_NullValue_ShouldReturnBadRequest() throws Exception {
        String amenitiesJson = """
            [
              "Free WiFi",
              null,
              "Pool"
            ]
            """;

        mockMvc.perform(post("/property-view/hotels/" + hotelId + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenitiesJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    public void testAddAmenities_DuplicateEntries_ShouldSucceed() throws Exception {
        String amenitiesJson = """
            [
              "Free WiFi",
              "Free WiFi",
              "Pool"
            ]
            """;

        mockMvc.perform(post("/property-view/hotels/" + hotelId + "/amenities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(amenitiesJson)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(hotelId));
    }
}