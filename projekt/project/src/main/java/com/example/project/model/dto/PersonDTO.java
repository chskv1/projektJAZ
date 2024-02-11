package com.example.project.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
public class PersonDTO {
    public PersonDTO() {
    }

    @NotBlank
    private Integer id;

    @NotBlank
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
}
