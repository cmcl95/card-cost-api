package com.cmcabrera.cardcostapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(description = "Error response details")
public class ApiErrorDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Schema(description = "Timestamp of the error", example = "12-07-2025 10:30:00")
    private LocalDateTime timestamp;
    @Schema(description = "HTTP status code", example = "400")
    private int status;
    @Schema(description = "Error message", example = "Bad Request")
    private String error;
    @Schema(description = "List of error details", example = "[\"Field 'cardNumber' must be between 8 and 19 characters\"]")
    private List<String> details;

    public ApiErrorDTO(int status, String error, List<String> details) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.details = details;
    }
}
