package com.example.project.model.mappers;

import com.example.project.model.Address;
import com.example.project.model.dto.AddressDTO;

public class AddressMapper {
    public static AddressDTO toDTO(Address address) {
        return new AddressDTO(
                address.getId(),
                address.getCountry(),
                address.getState(),
                address.getZipCode(),
                address.getCity(),
                address.getStreetName()
        );
    }

    public static Address toEntity(AddressDTO addressDTO) {
        return new Address(
                addressDTO.getId(),
                addressDTO.getCountry(),
                addressDTO.getState(),
                addressDTO.getZipCode(),
                addressDTO.getCity(),
                addressDTO.getStreetName()
        );
    }
}
