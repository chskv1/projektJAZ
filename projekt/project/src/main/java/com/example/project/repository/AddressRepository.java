package com.example.project.repository;

import com.example.project.model.Address;
import com.example.project.model.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findAddressesByPerson(Person person);

    @Modifying
    @Transactional
    void deleteAddressByStateAndCity(String state, String city);
}
