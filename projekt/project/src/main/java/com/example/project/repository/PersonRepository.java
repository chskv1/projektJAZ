package com.example.project.repository;

import com.example.project.model.Address;
import com.example.project.model.Person;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findById(int id);

    @Transactional
    @Modifying
    @Query("UPDATE Person p SET p.firstName = :firstName, " +
            "p.secondName = :secondName, " +
            "p.email = :email, " +
            "p.gender = :gender, " +
            "p.username = :username WHERE p.id = :id")
    void updateById(@Param("firstName") String firstName,
                    @Param("secondName") String secondName,
                    @Param("email") String email,
                    @Param("gender") String gender,
                    @Param("username") String username,
                    @Param("id") int id);
}
