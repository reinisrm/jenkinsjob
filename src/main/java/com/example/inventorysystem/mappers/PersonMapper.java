package com.example.inventorysystem.mappers;

import com.example.inventorysystem.models.dto.PersonDTO;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.models.User;

public class PersonMapper {
    public static PersonDTO toDTO(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setPersonId(person.getPersonId());
        dto.setName(person.getName());
        dto.setSurname(person.getSurname());
        dto.setPhoneNumber(person.getPhoneNumber());
        dto.setCourseName(person.getCourseName());
        if (person.getUser() != null) {
            dto.setUserId(person.getUser().getUserId());
        }
        return dto;
    }

    public static Person toEntity(PersonDTO dto, User user) {
        Person person = new Person();
        person.setPersonId(dto.getPersonId());
        person.setName(dto.getName());
        person.setSurname(dto.getSurname());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setCourseName(dto.getCourseName());
        person.setUser(user);
        return person;
    }
}
