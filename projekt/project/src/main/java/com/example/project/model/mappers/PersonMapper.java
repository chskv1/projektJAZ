package com.example.project.model.mappers;

import com.example.project.model.Address;
import com.example.project.model.Person;
import com.example.project.model.dto.PersonDTO;
import com.example.project.model.dto.PersonWithAddressesDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PersonMapper {
    public static PersonDTO toDTO(Person person) {
        return new PersonDTO(
                person.getId(),
                person.getFirstName(),
                person.getSecondName(),
                person.getEmail(),
                person.getGender(),
                person.getUsername()
        );
    }

    public static PersonWithAddressesDTO toDTOwithAddrresses(Person person) {

        var cities = person.getAddresses().stream().map(Address::getCity).toList();
        var streetNames = person.getAddresses().stream().map(Address::getStreetName).toList();

        List<String> moreData = new ArrayList<>();
        for(int i = 0; i < cities.size(); i++)
            moreData.add(cities.get(i) + ": " + streetNames.get(i));


        return new PersonWithAddressesDTO(
                person.getId(),
                person.getFirstName(),
                person.getSecondName(),
                person.getEmail(),
                person.getGender(),
                person.getUsername(),
                moreData
        );
    }


    public static Person toEntity(PersonDTO personDTO) {
        return new Person(
                personDTO.getId(),
                personDTO.getFirstName(),
                personDTO.getSecondName(),
                personDTO.getEmail(),
                personDTO.getGender(),
                personDTO.getUsername()
        );
    }
}
