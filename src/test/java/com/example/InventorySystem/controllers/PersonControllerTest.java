package com.example.InventorySystem.controllers;

import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonServiceImpl personService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    private List<Person> personList;

    @BeforeEach
    void setUp() {
        personList = new ArrayList<>();
        Person person1 = new Person("John", "Doe", "123456789", "2PS");
        Person person2 = new Person("Jane", "Doe", "987654321", "1PS");
        personList.add(person1);
        personList.add(person2);
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    void testShowAllPerson() {
        when(personService.getAll()).thenReturn(personList);

        String viewName = personController.showAllPerson(model);

        verify(model).addAttribute(eq("persons"), eq(personList));
        assertEquals("show-all-person", viewName);
    }

    @Test
    void testShowOnePerson() throws Exception {
        // Create a sample person
        Person person = new Person();
        person.setPersonId(1);
        person.setName("Sample");
        person.setSurname("Person");
        person.setPhoneNumber("123456789");
        person.setCourseName("2PS");

        when(personService.getPersonById(anyInt())).thenReturn(Optional.of(person));

        // GET
        mockMvc.perform(get("/person/{personId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("show-one-person"))
                .andExpect(model().attribute("person", Optional.of(person)));
    }

    @Test
    void testCreatePersonForm() {
        String viewName = personController.createPersonForm(model);

        verify(model).addAttribute(eq("person"), any(Person.class));
        assertEquals("create-person", viewName);
    }

    @Test
    void testCreatePersonSuccess() {
        Person person = new Person("John", "Doe", "123456789", "2PS");
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = personController.createPerson(person, bindingResult);

        assertEquals("redirect:/person/", viewName);
        verify(personService).createPerson(any(Person.class));
    }

    @Test
    void testCreatePersonValidationFailed() {
        Person person = new Person();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = personController.createPerson(person, bindingResult);

        assertEquals("create-person", viewName);
    }

    @Test
    void testShowUpdateForm() {
        // Arrange
        int personId = 1;
        Person person = new Person();
        Mockito.when(personService.getPersonById(anyInt())).thenReturn(Optional.of(person));

        String result = personController.showUpdateForm(personId, model);

        assertEquals("person-update-page", result);
        Mockito.verify(model).addAttribute("person", Optional.of(person));
    }

    @Test
    void testUpdatePersonByIdValidationFailed() {
        int personId = 1;
        Person updatedPerson = new Person();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = personController.updatePersonById(personId, updatedPerson, bindingResult);

        assertEquals("person-update-page", viewName);
    }

    @Test
    void testDeletePersonById() {
        int personId = 1;

        String viewName = personController.deletePersonById(personId);

        assertEquals("redirect:/person/", viewName);
        verify(personService).deletePersonById(personId);
    }
}