package com.example.project.services;

import com.example.project.exceptions.PersonNotFound;
import com.example.project.model.Address;
import com.example.project.model.dto.AddressDTO;
import com.example.project.model.dto.PersonDTO;
import com.example.project.model.dto.PersonWithAddressesDTO;
import com.example.project.model.mappers.AddressMapper;
import com.example.project.model.mappers.PersonMapper;
import com.example.project.repository.AddressRepository;
import com.example.project.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    public long getPersonsCount() {
        return this.personRepository.count();
    }

    public long getAddressesCount() {
        return this.addressRepository.count();
    }

    public List<PersonDTO> getAllPersonsWithName(String name)
    {
        return this.personRepository
                .findAll()
                .stream()
                .filter(person -> person.getFirstName().equals(name))
                .map(PersonMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PersonDTO> getAll() {
        return this.personRepository
                .findAll()
                .stream()
                .map(PersonMapper::toDTO)
                .collect(Collectors.toList());
    }

    public PersonDTO getById(int id) {
        return PersonMapper.toDTO(this.personRepository.findById(id).orElseThrow(
                () -> new PersonNotFound(id)));
    }

    public PersonWithAddressesDTO getByIdWithAddress(int id) {
        return PersonMapper.toDTOwithAddrresses(this.personRepository.findById(id).orElseThrow(
                () -> new PersonNotFound(id))
        );
    }

    @Transactional
    public void save(PersonDTO person) {
        personRepository.save(PersonMapper.toEntity(person));
    }

    @Transactional
    public void update(PersonDTO person, int personId) {
        personRepository.updateById(person.getFirstName(),
                person.getSecondName(),
                person.getEmail(),
                person.getGender(),
                person.getUsername(),
                personId);
    }

    @Transactional
    public void delete(int personId) {
        personRepository.deleteById(personId);
    }

    @Transactional
    public void addAddress(int personId, AddressDTO addressDTO) {
        Address address = AddressMapper.toEntity(addressDTO);
        address.setPerson(personRepository.findById(personId).orElseThrow(
                () -> new PersonNotFound(personId)
        ));
        addressRepository.save(address);
    }

    public List<AddressDTO> getAddresses(int personId) {
        return addressRepository
                .findAddressesByPerson(PersonMapper.toEntity(getById(personId)))
                .stream()
                .map(AddressMapper::toDTO)
                .collect(Collectors.toList());
    }
}
