package com.example.project.updater.clients;

import com.example.project.updater.model.RandomPersonDTO;

import java.util.List;

public interface IRandomDataClient {

    public List<RandomPersonDTO> getRandomPeople(int size);

}
