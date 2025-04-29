package com.example.InventorySystem.service;

import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.models.User;
import com.example.InventorySystem.repos.PersonRepo;
import com.example.InventorySystem.repos.UserRepo;
import com.example.InventorySystem.services.LendingService;
import com.example.InventorySystem.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

    @InjectMocks
    private PersonServiceImpl personService;

    @Mock
    private PersonRepo personRepo;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Person> expectedPersons = new ArrayList<>();
        expectedPersons.add(new Person());
        when(personRepo.findAll()).thenReturn(expectedPersons);

        List<Person> actualPersons = personService.getAll();

        assertEquals(expectedPersons, actualPersons);
    }

    @Test
    void testGetPersonById_ExistingId() {
        int personId = 1;
        Person expectedPerson = new Person();
        when(personRepo.findById(personId)).thenReturn(Optional.of(expectedPerson));

        Optional<Person> actualPersonOptional = personService.getPersonById(personId);
        assertTrue(actualPersonOptional.isPresent());
        assertEquals(expectedPerson, actualPersonOptional.get());
    }

    @Test
    void testGetPersonById_NonExistingId() {
        int personId = 999;
        when(personRepo.findById(personId)).thenReturn(Optional.empty());

        Optional<Person> actualPersonOptional = personService.getPersonById(personId);
        assertTrue(actualPersonOptional.isEmpty());
    }

    @Test
    void testCreatePerson_WithValidUser() {
        Person person = new Person();
        int userId = 1;
        User user = new User();
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));

        personService.createPerson(person, userId);

        verify(personRepo).save(person);
        assertEquals(user, person.getUser());
    }

    @Test
    void testUpdatePersonById_ExistingId() {
        int personId = 1;
        Person existingPerson = new Person();
        existingPerson.setBorrowing(new ArrayList<>());
        existingPerson.setLending(new ArrayList<>());

        Person updatedPersonData = new Person();
        updatedPersonData.setName("John");

        when(personRepo.findById(personId)).thenReturn(Optional.of(existingPerson));
        when(personRepo.save(any(Person.class))).thenReturn(existingPerson);

        personService.updatePersonById(personId, updatedPersonData);

        verify(personRepo).save(existingPerson);
        assertEquals("John", existingPerson.getName());
    }

    @Test
    void testDeletePersonById_ExistingId() {
        int personId = 1;
        Person person = new Person();
        person.setBorrowing(new ArrayList<>());
        person.setLending(new ArrayList<>());
        when(personRepo.findById(personId)).thenReturn(Optional.of(person));

        personService.deletePersonById(personId);

        verify(personRepo).deleteById(personId);
    }

    @Test
    void testDeletePersonById_NonExistingId() {
        int personId = 999;
        when(personRepo.findById(personId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> personService.deletePersonById(personId));
        verify(personRepo, never()).deleteById(personId);
    }

    @Test
    void testCreatePerson_NullInput_ThrowsException() {
        int tempUserId = 1;

        assertThrows(IllegalArgumentException.class, () -> personService.createPerson(null, tempUserId));

        verify(personRepo, never()).save(any());
        verify(userRepo, never()).findById(anyInt());
    }
}