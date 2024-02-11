package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "persons")
public class Person {
    @Id
    @Column(name = "person_id")
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name =  "second_name")
    private String secondName;

    @Column(name = "email")
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    public Person(int id, String firstName, String secondName, String email, String gender, String username) {
        this.id = id;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.gender = gender;
        this.username = username;
    }

    public Person() {
    }
}
