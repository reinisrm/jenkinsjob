package com.example.inventorysystem.controllers;

import com.example.inventorysystem.config.UserDetailsManagerImpl;
import com.example.inventorysystem.constants.ViewNames;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.models.User;
import com.example.inventorysystem.services.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import javax.swing.text.View;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final Logger log = LoggerFactory.getLogger(PersonController.class);

    private final PersonServiceImpl personService;
    private final UserDetailsManagerImpl userDetailsManager;

    @Autowired
    public PersonController(PersonServiceImpl personService, UserDetailsManagerImpl userDetailsManager) {
        this.personService = personService;
        this.userDetailsManager = userDetailsManager;
    }

    @GetMapping("/")
    public String showAllPerson(Model model) {
        log.info("Get person list");
        try {
            List<Person> persons = personService.getAll();
            log.info("Person list size: {}", persons.size());
            model.addAttribute("persons", persons);
            return ViewNames.SHOW_ALL_PERSON;
        } catch (Exception e) {
            log.error("Error occurred while fetching all persons", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/{personId}")
    public String showOnePerson(@PathVariable("personId") int personId, Model model) {
        log.info("Get person with id: {}", personId);
        Optional<Person> person = personService.getPersonById(personId);

        if(person.isPresent()) {
            Person personEntity = person.get(); // Used for logging name+surname
            String nameSurname = personEntity.getName() + " " + personEntity.getSurname(); // Same applies here
            model.addAttribute(ViewNames.PERSON, person);
            log.info("Received person: {}", nameSurname);
            return ViewNames.SHOW_ONE_PERSON;
        } else {
            log.info("Person with id: {} not found", personId);
            return ViewNames.ERROR;
        }
    }


    @GetMapping("/create")
    public String createPersonForm(Model model) {
        log.info("Creating new person");
        List<User> users = userDetailsManager.allUsers();
        model.addAttribute(ViewNames.PERSON, new Person());
        model.addAttribute("users", users);
        return ViewNames.CREATE_PERSON;
    }

    @PostMapping("/create")
    public String createPerson(@Valid Person person, BindingResult result, @RequestParam int userId) {
        if (result.hasErrors()) {
            log.error("Failed creating a new person, error: {}", result);
            return ViewNames.CREATE_PERSON;
        }

        try {
            personService.createPerson(person, userId);
            log.info("Person created successfully with user id: {}", userId);
            return ViewNames.REDIRECT_PERSON;
        } catch (Exception e) {
            log.error("Error occurred while creating person", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/update/{personId}")
    public String showUpdateForm(@PathVariable("personId") int personId, Model model) {
        try {
            Optional<Person> person = personService.getPersonById(personId);
            model.addAttribute(ViewNames.PERSON, person);
            return ViewNames.PERSON_UPDATE;
        } catch (Exception e) {
            log.error("Error occurred while preparing update form for person with ID: {}", personId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/update/{personId}")
    public String updatePersonById(@PathVariable("personId") int personId, @Valid Person person,
                                   BindingResult result) {
        if (personId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", personId);
            return ViewNames.PERSON_UPDATE;
        }

        if (result.hasErrors()) {
            log.warn("Error updating person: {}", result.getAllErrors());
            return ViewNames.PERSON_UPDATE;
        }

        try {
            personService.updatePersonById(personId, person);
            log.info("Person with id: {} has been updated", personId);
            return "redirect:/person/{personId}";
        } catch (Exception e) {
            log.error("Error occurred while updating person with ID: {}", personId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/delete/{personId}")
    public String deletePersonById(@PathVariable("personId") int personId) {

        if (personId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", personId);
            return ViewNames.SHOW_ONE_PERSON;
        }
        Optional<Person> person = personService.getPersonById(personId);
        if (!person.isPresent()) {
            log.warn("Person with id: {} for delete is not found", personId);
            return ViewNames.SHOW_ONE_PERSON;
        }

        personService.deletePersonById(personId);
        log.info("Person with id: {} is deleted", personId);
        return ViewNames.REDIRECT_PERSON;
    }
}