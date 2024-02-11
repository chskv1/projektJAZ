package com.example.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class PersonWithAddressesDTO {
    public PersonWithAddressesDTO() {
    }

    private Integer id;

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("second_name")
    private String secondName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("username")
    private String username;

    @JsonProperty("addresses")
    private List<String> addresses;
}
