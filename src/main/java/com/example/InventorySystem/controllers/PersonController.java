package com.example.InventorySystem.controllers;

import com.example.InventorySystem.config.UserDetailsManagerImpl;
import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.models.User;
import com.example.InventorySystem.services.impl.PersonServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/person")
public class PersonController {

    private final Logger log = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonServiceImpl personService;

    @Autowired
    UserDetailsManagerImpl userDetailsManager;

    @GetMapping("/")
    public String showAllPerson(Model model) {
        log.info("Get person list");
        try {
            List<Person> persons = personService.getAll();
            log.info("Person list size: {}", persons.size());
            model.addAttribute("persons", persons);
            return "show-all-person";
        } catch (Exception e) {
            log.error("Error occurred while fetching all persons", e);
            return "error";
        }
    }

    @GetMapping("/{personId}")
    public String showOnePerson(@PathVariable("personId") int personId, Model model) {
        log.info("Get person with id: {}", personId);
        Optional<Person> person = personService.getPersonById(personId);

        if(person.isPresent()) {
            Person personEntity = person.get(); // Used for logging name+surname
            String nameSurname = personEntity.getName() + " " + personEntity.getSurname(); // Same applies here
            model.addAttribute("person", person);
            log.info("Received person: {}", nameSurname);
            return "show-one-person";
        } else {
            log.info("Person with id: {} not found", personId);
            return "error";
        }
    }


    @GetMapping("/create")
    public String createPersonForm(Model model) {
        log.info("Creating new person");
        List<User> users = userDetailsManager.allUsers();
        model.addAttribute("person", new Person());
        model.addAttribute("users", users);
        return "create-person";
    }

    @PostMapping("/create")
    public String createPerson(@Valid Person person, BindingResult result, @RequestParam int userId) {
        if (result.hasErrors()) {
            log.error("Failed creating a new person, error: {}", result);
            return "create-person";
        }

        try {
            personService.createPerson(person, userId);
            log.info("Person created successfully with user id: {}", userId);
            return "redirect:/person/";
        } catch (Exception e) {
            log.error("Error occurred while creating person", e);
            return "error";
        }
    }

    @GetMapping("/update/{personId}")
    public String showUpdateForm(@PathVariable("personId") int personId, Model model) {
        try {
            Optional<Person> person = personService.getPersonById(personId);
            model.addAttribute("person", person);
            return "person-update-page";
        } catch (Exception e) {
            log.error("Error occurred while preparing update form for person with ID: {}", personId, e);
            return "error";
        }
    }

    @PostMapping("/update/{personId}")
    public String updatePersonById(@PathVariable("personId") int personId, @Valid Person person,
                                   BindingResult result) {
        if (personId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", personId);
            return "person-update-page";
        }

        if (result.hasErrors()) {
            log.warn("Error updating person: {}", result.getAllErrors());
            return "person-update-page";
        }

        try {
            personService.updatePersonById(personId, person);
            log.info("Person with id: {} has been updated", personId);
            return "redirect:/person/{personId}";
        } catch (Exception e) {
            log.error("Error occurred while updating person with ID: {}", personId, e);
            return "error";
        }
    }

    @PostMapping("/delete/{personId}")
    public String deletePersonById(@PathVariable("personId") int personId) {

        if (personId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", personId);
            return "show-one-person"; //TODO check if endpoint=correct
        }
        Optional<Person> person = personService.getPersonById(personId);
        if (!person.isPresent()) {
            log.warn("Person with id: {} for delete is not found", personId);
            return "show-one-person"; //TODO same here
        }

        personService.deletePersonById(personId);
        log.info("Person with id: {} is deleted", personId);
        return "redirect:/person/";
    }
}
