package com.example.project.exceptions.advicers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class ExceptionDTO {
    public ExceptionDTO() {
    }

    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("information")
    private String information;
}