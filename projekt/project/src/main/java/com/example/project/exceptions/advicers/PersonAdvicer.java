package com.example.project.exceptions.advicers;

import com.example.project.exceptions.PersonNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PersonAdvicer {

    @ResponseBody
    @ExceptionHandler(PersonNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<ExceptionDTO> personNotFoundHandler(PersonNotFound ex) {
        return new ResponseEntity<>(new ExceptionDTO("Person could not be found!", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
