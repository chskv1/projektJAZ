package com.example.project.updater;

import com.example.project.model.mappers.AddressMapper;
import com.example.project.model.mappers.PersonMapper;
import com.example.project.repository.AddressRepository;
import com.example.project.repository.PersonRepository;
import com.example.project.updater.clients.IRandomDataClient;
import com.example.project.updater.model.RandomPersonDTO;
import com.example.project.updater.model.RandomPersonMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonUpdater {

    private final IRandomDataClient dataClient;
    private final PersonRepository personRepository;
    private final AddressRepository addressRepository;

    public PersonUpdater(IRandomDataClient dataClient, PersonRepository personRepository, AddressRepository addressRepository) {
        this.dataClient = dataClient;
        this.personRepository = personRepository;
        this.addressRepository = addressRepository;
    }

    @PostConstruct
    public void init() {
        updatePeople(20);
    }

    public void updatePeople(int size) {
        List<RandomPersonDTO> randomPeople = dataClient.getRandomPeople(size);

        //Need mapper to proceed
        PersonMapper personMapper = new PersonMapper();
        AddressMapper addressMapper = new AddressMapper();

        randomPeople.stream()
                //Convert them to PersonDTO
                .map(RandomPersonMapper::toPersonDTO)
                //Save each as entity
                .forEach(personDTO -> personRepository.save(personMapper.toEntity(personDTO)));

        //Data for AddressDTO is in RandomPersonDTO (like in PDF)
        randomPeople.stream()
                // Retrieve all RandomAddressDTO
                .map(randomPersonDTO -> randomPersonDTO.getAddress())
                // Convert them to AddressDTO
                .map(RandomPersonMapper::toAddressDTO)
                // Save each as entity
                .forEach(addressDTO -> addressRepository.save(addressMapper.toEntity(addressDTO)));
    }
}