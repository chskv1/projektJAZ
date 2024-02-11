package com.example.project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "addresses")
public class Address {
    @Id
    @Column(name = "address_id")
    private Integer id;

    @Column(name = "country")
    private String country;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "city")
    private String city;

    @Column(name = "street_name")
    private String streetName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private Person person;

    public Address(Integer id, String country, String state, String zipCode, String city, String streetName) {
        this.id = id;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
        this.city = city;
        this.streetName = streetName;
    }

    public Address() {
    }
}
