package com.example.project.updater.model;

import com.example.project.model.dto.AddressDTO;
import com.example.project.model.dto.PersonDTO;
import org.json.JSONObject;

public class RandomPersonMapper {

    public static RandomPersonDTO toDTO(JSONObject jsonObject) {
        return new RandomPersonDTO(
                jsonObject.getInt("id"),
                jsonObject.getString("first_name"),
                jsonObject.getString("last_name"),
                jsonObject.getString("email"),
                jsonObject.getString("gender"),
                jsonObject.getString("username"),
                new RandomAddressDTO(jsonObject.getInt("id"),
                        new JSONObject(jsonObject.get("address").toString()).getString("country"),
                        new JSONObject(jsonObject.get("address").toString()).getString("state"),
                        new JSONObject(jsonObject.get("address").toString()).getString("zip_code"),
                        new JSONObject(jsonObject.get("address").toString()).getString("city"),
                        new JSONObject(jsonObject.get("address").toString()).getString("street_name")
                ));
    }

    public static PersonDTO toPersonDTO(RandomPersonDTO randomPersonDTO)
    {
        return new PersonDTO(
                randomPersonDTO.getId(),
                randomPersonDTO.getFirstName(),
                randomPersonDTO.getSecondName(),
                randomPersonDTO.getEmail(),
                randomPersonDTO.getGender(),
                randomPersonDTO.getUsername()
        );
    }

    public static AddressDTO toAddressDTO(RandomAddressDTO randomAddressDTO)
    {
        return new AddressDTO(
                randomAddressDTO.getId(),
                randomAddressDTO.getCountry(),
                randomAddressDTO.getState(),
                randomAddressDTO.getZipCode(),
                randomAddressDTO.getCity(),
                randomAddressDTO.getStreetName()
        );
    }
}
