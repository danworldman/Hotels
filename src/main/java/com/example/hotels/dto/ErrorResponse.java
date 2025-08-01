package com.example.hotels.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ ошибки")
public class ErrorResponse {

    @Schema(description = "Описание ошибки", example = "Unsupported parameter: invalidParam")
    private String error;

    public ErrorResponse() {}

    public ErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}