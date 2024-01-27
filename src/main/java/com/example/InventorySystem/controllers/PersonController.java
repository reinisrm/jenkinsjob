package com.example.InventorySystem.controllers;

import com.example.InventorySystem.models.Person;
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

    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    @Autowired
    PersonServiceImpl personService;

    @GetMapping("/")
    public String showAllPerson(Model model) {
        try {
            List<Person> persons = personService.getAll();
            model.addAttribute("persons", persons);
            return "show-all-person";
        } catch (Exception e) {
            logger.error("Error occurred while fetching all persons", e);
            return "error";
        }
    }

    @GetMapping("/{personId}")
    public String showOnePerson(@PathVariable("personId") int personId, Model model) {
        try {
            Optional<Person> person = personService.getPersonById(personId);
            model.addAttribute("person", person);
            return "show-one-person";
        } catch (Exception e) {
            logger.error("Error occurred while fetching person with ID: {}", personId, e);
            return "error";
        }
    }

    @GetMapping("/create")
    public String createPersonForm(Model model) {
        model.addAttribute("person", new Person());
        return "create-person";
    }

    @PostMapping("/create")
    public String createPerson(@Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "create-person";
        }

        try {
            Person temp = new Person(
                    person.getName(),
                    person.getSurname(),
                    person.getPhoneNumber(),
                    person.getCourseName()
            );

            personService.createPerson(temp);
            return "redirect:/person/";
        } catch (Exception e) {
            logger.error("Error occurred while creating person", e);
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
            logger.error("Error occurred while preparing update form for person with ID: {}", personId, e);
            return "error";
        }
    }

    @PostMapping("/update/{personId}")
    public String updatePersonById(@PathVariable("personId") int personId, @Valid Person person,
                                   BindingResult result) {
        if (result.hasErrors()) {
            return "person-update-page";
        }

        try {
            personService.updatePersonById(personId, person);
            return "redirect:/person/{personId}";
        } catch (Exception e) {
            logger.error("Error occurred while updating person with ID: {}", personId, e);
            return "error";
        }
    }

    @PostMapping("/delete/{personId}")
    public String deletePersonById(@PathVariable("personId") int personId) {
        try {
            personService.deletePersonById(personId);
            return "redirect:/person/";
        } catch (Exception e) {
            logger.error("Error occurred while deleting person with ID: {}", personId, e);
            return "error";
        }
    }
}
