package com.example.project.updater.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class RandomAddressDTO {
    RandomAddressDTO() {
    }

    private Integer id;

    @JsonProperty("country")
    private String country;

    @JsonProperty("state")
    private String state;

    @JsonProperty("zip_code")
    private String zipCode;

    @JsonProperty("city")
    private String city;

    @JsonProperty("street_name")
    private String streetName;
}
