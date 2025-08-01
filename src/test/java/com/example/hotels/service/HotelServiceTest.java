package com.example.hotels.service;

import com.example.hotels.dto.HotelDetailDTO;
import com.example.hotels.dto.HotelSummaryDTO;
import com.example.hotels.repository.base.IAmenityBase;
import com.example.hotels.repository.base.IHotelBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HotelServiceTest {

    private IHotelBase hotelRepository;
    private IAmenityBase amenityRepository;
    private HotelService hotelService;

    @BeforeEach
    void setUp() {
        hotelRepository = Mockito.mock(IHotelBase.class);
        amenityRepository = Mockito.mock(IAmenityBase.class);
        hotelService = new HotelService(hotelRepository, amenityRepository);
    }

    @Test
    void testGetAllHotels_EmptyList() {
        when(hotelRepository.findAll()).thenReturn(List.of());

        List<HotelSummaryDTO> result = hotelService.getAllHotels();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(hotelRepository).findAll();
    }

    @Test
    void testGetHistogramBrand() {
        when(hotelRepository.getHistogram("brand")).thenReturn(Map.of(
                "BrandX", 2L,
                "Hilton", 3L
        ));

        Map<String, Long> result = hotelService.getHistogram("brand");

        assertEquals(2, result.size());
        assertEquals(2L, result.get("BrandX"));
        assertEquals(3L, result.get("Hilton"));
        verify(hotelRepository).getHistogram("brand");
    }

    @Test
    void testFindById_Found() {
        HotelDetailDTO dto = new HotelDetailDTO();
        dto.setId(1L);
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(dto));

        Optional<HotelDetailDTO> result = hotelService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(hotelRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(hotelRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<HotelDetailDTO> result = hotelService.findById(999L);

        assertFalse(result.isPresent());
        verify(hotelRepository).findById(999L);
    }

    @Test
    void testSearch() {
        List<HotelSummaryDTO> dummyList = List.of(new HotelSummaryDTO());
        when(hotelRepository.search("name", "brand", "city", "country", List.of("WiFi"))).thenReturn(dummyList);

        List<HotelSummaryDTO> result = hotelService.search("name", "brand", "city", "country", List.of("WiFi"));

        assertEquals(1, result.size());
        verify(hotelRepository).search("name", "brand", "city", "country", List.of("WiFi"));
    }

    @Test
    void testCreateHotel() {
        HotelDetailDTO inputDto = new HotelDetailDTO();
        inputDto.setName("TestHotel");

        HotelDetailDTO savedDto = new HotelDetailDTO();
        savedDto.setId(1L);
        savedDto.setName("TestHotel");

        when(hotelRepository.save(inputDto)).thenReturn(savedDto);

        HotelSummaryDTO summaryDTO = hotelService.createHotel(inputDto);

        assertNotNull(summaryDTO);
        assertEquals(1L, summaryDTO.getId());
        assertEquals("TestHotel", summaryDTO.getName());
        verify(hotelRepository).save(inputDto);
    }

    @Test
    void testAddAmenitiesToHotel_Success() {
        when(hotelRepository.addAmenitiesToHotel(1L, List.of("WiFi", "Pool"))).thenReturn(true);

        HotelDetailDTO detailDTO = new HotelDetailDTO();
        detailDTO.setId(1L);
        detailDTO.setName("TestHotel");

        when(hotelRepository.findById(1L)).thenReturn(Optional.of(detailDTO));

        Optional<HotelSummaryDTO> result = hotelService.addAmenitiesToHotel(1L, List.of("WiFi", "Pool"));

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(hotelRepository).addAmenitiesToHotel(1L, List.of("WiFi", "Pool"));
        verify(hotelRepository).findById(1L);
    }

    @Test
    void testAddAmenitiesToHotel_Fail() {
        when(hotelRepository.addAmenitiesToHotel(999L, List.of("WiFi", "Pool"))).thenReturn(false);

        Optional<HotelSummaryDTO> result = hotelService.addAmenitiesToHotel(999L, List.of("WiFi", "Pool"));

        assertTrue(result.isEmpty());
        verify(hotelRepository).addAmenitiesToHotel(999L, List.of("WiFi", "Pool"));
        verify(hotelRepository, never()).findById(anyLong());
    }

}