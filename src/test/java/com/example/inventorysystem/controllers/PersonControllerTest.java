package com.example.inventorysystem.controllers;

import com.example.inventorysystem.config.UserDetailsManagerImpl;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @InjectMocks
    private PersonController personController;

    @Mock
    private PersonServiceImpl personService;

    @Mock
    private UserDetailsManagerImpl userDetailsManager;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    private List<Person> personList;

    private Person setUpCreatePerson() {
        return new Person("John", "Doe", "123456789", "Computer Science");
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
    }

    @Test
    void testShowAllPerson() {
        List<Person> persons = new ArrayList<>();
        persons.add(setUpCreatePerson());
        when(personService.getAll()).thenReturn(persons);

        String viewName = personController.showAllPerson(model);

        verify(model).addAttribute("persons", persons);
        assertEquals("show-all-person", viewName);
    }

    @Test
    void testShowOnePerson() throws Exception {
        Person person = setUpCreatePerson();
        person.setPersonId(1);

        when(personService.getPersonById(person.getPersonId())).thenReturn(Optional.of(person));

        mockMvc.perform(get("/person/{personId}", person.getPersonId()))
                .andExpect(status().isOk())
                .andExpect(view().name("show-one-person"))
                .andExpect(model().attribute("person", Optional.of(person)));
    }

    @Test
    void testShowOnePersonNonExistentId() throws Exception {
        int nonExistentPersonId = 999;
        when(personService.getPersonById(nonExistentPersonId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/person/{personId}", nonExistentPersonId))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(personService).getPersonById(nonExistentPersonId);
        verify(model, never()).addAttribute(eq("person"), any());
    }

    @Test
    void testCreatePersonForm() {
        when(userDetailsManager.allUsers()).thenReturn(new ArrayList<>());

        String viewName = personController.createPersonForm(model);

        verify(model).addAttribute(eq("person"), any(Person.class));
        verify(userDetailsManager).allUsers();
        assertEquals("create-person", viewName);
    }

    @Test
    void testCreatePersonSuccess() throws Exception {
        Person person = setUpCreatePerson();

        mockMvc.perform(post("/person/create")
                        .param("name", person.getName())
                        .param("surname", person.getSurname())
                        .param("phoneNumber", person.getPhoneNumber())
                        .param("courseName", person.getCourseName())
                        .param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/person/"));

        verify(personService).createPerson(any(Person.class), eq(1));
    }

    @Test
    void testCreatePersonValidationFailed() {
        Person person = setUpCreatePerson();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = personController.createPerson(person, bindingResult, 1);

        assertEquals("create-person", viewName);
    }

    @Test
    void testShowUpdateForm() {
        Person person = setUpCreatePerson();
        person.setPersonId(1);
        when(personService.getPersonById(person.getPersonId())).thenReturn(Optional.of(person));

        String viewName = personController.showUpdateForm(person.getPersonId(), model);

        verify(model).addAttribute("person", Optional.of(person));
        assertEquals("person-update-page", viewName);
    }

    @Test
    void testUpdatePersonByIdValidationFailed() {
        Person person = setUpCreatePerson();
        person.setPersonId(1);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = personController.updatePersonById(person.getPersonId(), person, bindingResult);

        assertEquals("person-update-page", viewName);
    }

    @Test
    void testDeletePersonById() throws Exception {
        Person person = setUpCreatePerson();
        person.setPersonId(1);
        when(personService.getPersonById(person.getPersonId())).thenReturn(Optional.of(person));

        mockMvc.perform(post("/person/delete/{personId}", person.getPersonId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/person/"));

        verify(personService).deletePersonById(person.getPersonId());
    }
}
