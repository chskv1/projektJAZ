package com.example.project.controllers;

import com.example.project.model.Person;
import com.example.project.model.dto.AddressDTO;
import com.example.project.model.dto.PersonDTO;
import com.example.project.model.dto.PersonWithAddressesDTO;
import com.example.project.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping()
    public ResponseEntity<List<PersonDTO>> getAllPeople() {
        return new ResponseEntity<>(personService.getAll(), HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        personService.save(personDTO);
        return new ResponseEntity<>(personDTO, HttpStatus.CREATED);
    }

    @PostMapping("/with/address")
    public ResponseEntity<PersonDTO> createPersonWithAddress(@RequestBody PersonDTO personDTO, @RequestBody AddressDTO addressDTO)
    {
        personService.save(personDTO);
        personService.addAddress(personDTO.getId(), addressDTO);
        return new ResponseEntity<>(personDTO, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable int id) {
        return new ResponseEntity<>(personService.getById(id), HttpStatus.OK);
    }

    @GetMapping(params = "id")
    public ResponseEntity<PersonDTO> getPersonByIdInParam(@RequestParam int id) {
        return new ResponseEntity<>(personService.getById(id), HttpStatus.OK);
    }

    @GetMapping(params = {"id", "addresses"})
    public ResponseEntity<PersonWithAddressesDTO> getPersonByIdWithAddresses(@RequestParam int id, @RequestParam boolean addresses)
    {
        return new ResponseEntity<>(personService.getByIdWithAddress(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePersonById(@PathVariable int id, @RequestBody PersonDTO updatedPerson) {
            personService.getById(id);
            personService.update(updatedPerson, id);
            return ResponseEntity.ok(updatedPerson);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePersonById(@PathVariable int id) {
        personService.getById(id);
        personService.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddressesById(@PathVariable int id) {
        personService.getById(id);
        return new ResponseEntity<>(personService.getAddresses(id), HttpStatus.OK);
    }

    @PostMapping("/{id}/addresses")
    public ResponseEntity<AddressDTO> addAddress(@PathVariable int id, @RequestBody AddressDTO addressDTO) {
        personService.getById(id);
        personService.addAddress(id, addressDTO);
        return new ResponseEntity<>(addressDTO, HttpStatus.CREATED);
    }

}
