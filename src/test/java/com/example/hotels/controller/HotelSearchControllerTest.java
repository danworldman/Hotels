package com.example.hotels.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.hotels.HotelsApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = HotelsApplication.class)
@AutoConfigureMockMvc
public class HotelSearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSearchHotels_ByCity() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testSearchHotels_ByMultipleParams() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("city", "Minsk")
                        .param("brand", "Hilton")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testSearchHotels_NoMatches_ReturnsEmptyList() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("city", "NonExistingCity1234567")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    public void testSearchHotels_WithUnknownParam_ShouldHandleGracefully() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .param("unknownParam", "someValue")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testSearchHotels_EmptyParams_ReturnAllOrEmptyList() throws Exception {
        mockMvc.perform(get("/property-view/search")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}