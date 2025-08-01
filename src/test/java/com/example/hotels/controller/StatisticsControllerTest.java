package com.example.hotels.controller;

import com.example.hotels.service.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HotelService hotelService;

    @Test
    void testGetHistogramBrand() throws Exception {
        when(hotelService.getHistogram("brand")).thenReturn(Map.of("Hilton", 3L));

        mockMvc.perform(get("/property-view/histogram/brand")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hilton").value(3));
    }

    @Test
    void testGetHistogramCity() throws Exception {
        when(hotelService.getHistogram("city")).thenReturn(Map.of("Minsk", 2L));

        mockMvc.perform(get("/property-view/histogram/city")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk").value(2));
    }

    @Test
    void testGetHistogramCountry() throws Exception {
        when(hotelService.getHistogram("country")).thenReturn(Map.of("Belarus", 4L));

        mockMvc.perform(get("/property-view/histogram/country")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Belarus").value(4));
    }

    @Test
    void testGetHistogramAmenities() throws Exception {
        when(hotelService.getHistogram("amenities")).thenReturn(Map.of("Free WiFi", 5L));

        mockMvc.perform(get("/property-view/histogram/amenities")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.['Free WiFi']").value(5));
    }

    @Test
    void testGetHistogramInvalidParam() throws Exception {
        when(hotelService.getHistogram("invalid"))
                .thenThrow(new IllegalArgumentException("Unsupported parameter: invalid"));

        mockMvc.perform(get("/property-view/histogram/invalid")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Unsupported parameter: invalid"));
    }
}