package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.models.dto.PersonDTO;
import com.example.inventorysystem.repos.PersonRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonRepo personRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        Person person = new Person();
        List<Person> persons = List.of(person);
        when(personRepo.findAll()).thenReturn(persons);

        List<PersonDTO> result = personService.getAll();

        assertEquals(1, result.size());
        verify(personRepo).findAll();
    }

    @Test
    void testGetPersonById_Existing() {
        Person person = new Person();
        person.setPersonId(1);
        when(personRepo.findById(1)).thenReturn(Optional.of(person));

        Optional<PersonDTO> result = personService.getPersonById(1);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getPersonId());
    }

    @Test
    void testGetPersonById_NonExisting() {
        when(personRepo.findById(999)).thenReturn(Optional.empty());

        Optional<PersonDTO> result = personService.getPersonById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreatePerson_Valid() {
        PersonDTO dto = new PersonDTO();
        dto.setName("Jane");

        personService.createPerson(dto);

        verify(personRepo).save(any(Person.class));
    }

    @Test
    void testUpdatePersonById_Existing() {
        int personId = 1;

        Person existing = new Person();
        existing.setPersonId(personId);

        PersonDTO dto = new PersonDTO();
        dto.setName("Updated");

        when(personRepo.findById(personId)).thenReturn(Optional.of(existing));
        when(personRepo.save(any())).thenReturn(existing);

        personService.updatePersonById(personId, dto);

        verify(personRepo).save(existing);
    }

    @Test
    void testUpdatePersonById_NonExisting() {
        when(personRepo.findById(999)).thenReturn(Optional.empty());

        personService.updatePersonById(999, new PersonDTO());

        verify(personRepo, never()).save(any());
    }

    @Test
    void testDeletePersonById_Existing() {
        Person person = new Person();

        when(personRepo.findById(1)).thenReturn(Optional.of(person));

        personService.deletePersonById(1);

        verify(personRepo).deleteById(1);
    }

    @Test
    void testDeletePersonById_NonExisting() {
        when(personRepo.findById(999)).thenReturn(Optional.empty());

        personService.deletePersonById(999);

        verify(personRepo, never()).deleteById(any());
    }
}