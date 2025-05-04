package com.example.inventorysystem.controllers;

import com.example.inventorysystem.models.dto.PersonDTO;
import com.example.inventorysystem.repos.PersonRepo;
import com.example.inventorysystem.services.impl.PersonServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private PersonServiceImpl personService;

    private PersonDTO person;

    @BeforeEach
    void setup() {
        personRepo.deleteAll();

        person = new PersonDTO();
        person.setName("Reinis");
        person.setSurname("Malitis");
        person.setPhoneNumber("+37125554320");
        person.setCourseName("1PS");
        personService.createPerson(person);
    }

    @Test
    @WithMockUser
    void testShowAllPerson() throws Exception {
        mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Reinis")));
    }

    @Test
    @WithMockUser
    void testShowOnePerson() throws Exception {
        int id = personService.getAll().get(0).getPersonId();

        mockMvc.perform(get("/person/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Reinis")));
    }

    @Test
    @WithMockUser
    void testCreatePersonForm() throws Exception {
        mockMvc.perform(get("/person/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("form")));
    }

    @Test
    @WithMockUser
    void testCreatePersonValidSubmission() throws Exception {
        mockMvc.perform(post("/person/create")
                        .param("name", "Anna")
                        .param("surname", "Smith")
                        .param("phoneNumber", "+37120000000")
                        .param("courseName", "2PS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/person/"));
    }

    @Test
    @WithMockUser
    void testShowUpdateForm() throws Exception {
        int id = personService.getAll().get(0).getPersonId();

        mockMvc.perform(get("/person/update/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Reinis")));
    }

    @Test
    @WithMockUser
    void testUpdatePersonValid() throws Exception {
        int id = personService.getAll().get(0).getPersonId();

        mockMvc.perform(post("/person/update/" + id)
                        .param("name", "UpdatedName")
                        .param("surname", "Malitis")
                        .param("phoneNumber", "+37125554320")
                        .param("courseName", "1PS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/person/" + id));
    }

    @Test
    @WithMockUser
    void testDeletePersonById() throws Exception {
        int id = personService.getAll().get(0).getPersonId();

        mockMvc.perform(post("/person/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/person/"));
    }

}
