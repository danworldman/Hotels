package com.example.hotels.controller;

import com.example.hotels.dto.ErrorResponse;
import com.example.hotels.dto.HotelDetailDTO;
import com.example.hotels.dto.HotelSummaryDTO;
import com.example.hotels.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.net.URI;

@RestController
@RequestMapping("/property-view")
public class HotelController {

    private final HotelService hotelService;

    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(summary = "Получить список всех отелей с краткой информацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список отелей",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDTO.class)))
    })
    @GetMapping("/hotels")
    public List<HotelSummaryDTO> getAllHotels() {
        return hotelService.getAllHotels();
    }

    @Operation(summary = "Получить подробную информацию по отелю по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Детали отеля",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelDetailDTO.class))),
            @ApiResponse(responseCode = "404", description = "Отель не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/hotels/{id}")
    public ResponseEntity<?> getHotelById(@PathVariable Long id) {
        Optional<HotelDetailDTO> hotelOpt = hotelService.findById(id);
        if (hotelOpt.isPresent()) {
            return ResponseEntity.ok(hotelOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Hotel with id " + id + " not found"));
        }
    }

    @Operation(summary = "Поиск отелей с фильтрацией по параметрам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список отелей, соответствующих критериям",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные параметры запроса",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @Parameter(description = "Название отеля") @RequestParam(required = false) String name,
            @Parameter(description = "Бренд отеля") @RequestParam(required = false) String brand,
            @Parameter(description = "Город") @RequestParam(required = false) String city,
            @Parameter(description = "Страна") @RequestParam(required = false) String country,
            @Parameter(description = "Удобства, через запятую") @RequestParam(required = false) String amenities
    ) {
        // Валидация длины строковых параметров
        if (name != null && name.trim().length() > 0 && name.trim().length() < 2) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Parameter 'name' must be at least 2 characters long"));
        }
        if (brand != null && brand.trim().length() > 0 && brand.trim().length() < 2) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Parameter 'brand' must be at least 2 characters long"));
        }
        if (city != null && city.trim().length() > 0 && city.trim().length() < 2) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Parameter 'city' must be at least 2 characters long"));
        }
        if (country != null && country.trim().length() > 0 && country.trim().length() < 2) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Parameter 'country' must be at least 2 characters long"));
        }

        List<String> amenityList = null;
        if (amenities != null && !amenities.trim().isEmpty()) {
            amenityList = Arrays.stream(amenities.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());

            boolean invalidAmenity = amenityList.stream().anyMatch(a -> a.length() < 2);
            if (invalidAmenity) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Each amenity must be at least 2 characters long"));
            }
        }

        List<HotelSummaryDTO> results = hotelService.search(name, brand, city, country, amenityList);
        return ResponseEntity.ok(results);
    }

    @Operation(summary = "Создание нового отеля")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Отель создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/hotels")
    public ResponseEntity<HotelSummaryDTO> createHotel(@Valid @RequestBody HotelDetailDTO hotelDetailDTO) {
        HotelSummaryDTO createdHotel = hotelService.createHotel(hotelDetailDTO);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdHotel.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdHotel);
    }


    @Operation(summary = "Добавить список удобств (amenities) к отелю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Обновленные данные отеля",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = HotelSummaryDTO.class))),
            @ApiResponse(responseCode = "400", description = "Пустой список удобств",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные элементы в списке удобств",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Отель не найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/hotels/{id}/amenities")
    public ResponseEntity<?> addAmenitiesToHotel(@PathVariable Long id, @Valid @RequestBody List<String> amenities) {
        if (amenities == null || amenities.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Amenities list must not be empty"));
        }

        boolean invalidAmenity = amenities.stream()
                .anyMatch(a -> a == null || a.trim().length() < 2);

        if (invalidAmenity) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Each amenity must be at least 2 characters long"));
        }

        Optional<HotelSummaryDTO> updatedHotelOpt = hotelService.addAmenitiesToHotel(id, amenities);

        if (updatedHotelOpt.isPresent()) {
            return ResponseEntity.ok(updatedHotelOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Hotel with id " + id + " not found"));
        }
    }
}