package com.example.project.exceptions;

public class PersonNotFound extends RuntimeException{
    public PersonNotFound(int id) {
        super("Person with that id has not been founded: " + id);
    }
}
