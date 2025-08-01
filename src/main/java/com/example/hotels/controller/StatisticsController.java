package com.example.hotels.controller;

import com.example.hotels.dto.ErrorResponse;
import com.example.hotels.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/property-view")
public class StatisticsController {

    private final HotelService hotelService;

    public StatisticsController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @Operation(
            summary = "Получить гистограмму по выбранному параметру",
            description = "Возвращает распределение количества отелей, сгруппированных по параметрам brand, city, country или amenities"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Успешное получение гистограммы",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Неверный параметр группировки",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/histogram/{param}")
    public ResponseEntity<Object> getHistogram(
            @Parameter(
                    description = "Параметр для группировки (brand, city, country, amenities)",
                    example = "brand",
                    required = true
            )
            @PathVariable String param) {
        try {
            Map<String, Long> histogram = hotelService.getHistogram(param);
            return ResponseEntity.ok(histogram);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }
}