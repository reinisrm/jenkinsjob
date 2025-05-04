package com.example.inventorysystem.services;

import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.models.dto.PersonDTO;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<PersonDTO> getAll();
    void createPerson(PersonDTO dto);
    Optional<PersonDTO> getPersonById(int personId);
    void updatePersonById(int personId, PersonDTO dto);
    void deletePersonById(int personId);
}
