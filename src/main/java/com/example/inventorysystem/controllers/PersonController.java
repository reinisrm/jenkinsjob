package com.example.inventorysystem.controllers;

import com.example.inventorysystem.constants.ViewNames;
import com.example.inventorysystem.models.dto.PersonDTO;
import com.example.inventorysystem.services.impl.PersonServiceImpl;
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

    private final PersonServiceImpl personService;

    @Autowired
    public PersonController(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @GetMapping("/")
    public String showAllPerson(Model model) {
        log.info("Fetching all persons");
        try {
            List<PersonDTO> persons = personService.getAll();
            log.info("Number of persons retrieved: {}", persons.size());
            model.addAttribute("persons", persons);
            return ViewNames.SHOW_ALL_PERSON;
        } catch (Exception e) {
            log.error("Error occurred while fetching all persons", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/{personId}")
    public String showOnePerson(@PathVariable("personId") int personId, Model model) {
        log.info("Fetching person with ID: {}", personId);
        Optional<PersonDTO> personDTO = personService.getPersonById(personId);

        if (personDTO.isPresent()) {
            log.info("Retrieved person: {} {}", personDTO.get().getName(), personDTO.get().getSurname());
            model.addAttribute(ViewNames.PERSON, personDTO);
            return ViewNames.SHOW_ONE_PERSON;
        } else {
            log.warn("Person with ID: {} not found", personId);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/create")
    public String createPersonForm(Model model) {
        log.info("Rendering form to create a new person");
        try {
            model.addAttribute(ViewNames.PERSON, new PersonDTO());
            return ViewNames.CREATE_PERSON;
        } catch (Exception e) {
            log.error("Error occurred while preparing create person form", e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/create")
    public String createPerson(@Valid @ModelAttribute(ViewNames.PERSON) PersonDTO personDTO,
                               BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Validation failed while creating a new person: {}", result.getAllErrors());
            return ViewNames.CREATE_PERSON;
        }

        try {
            personService.createPerson(personDTO);
            log.info("Person created successfully");
            return ViewNames.REDIRECT_PERSON;
        } catch (Exception e) {
            log.error("Error occurred while creating person", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/update/{personId}")
    public String showUpdateForm(@PathVariable("personId") int personId, Model model) {
        log.info("Preparing update form for person with ID: {}", personId);
        try {
            Optional<PersonDTO> personOpt = personService.getPersonById(personId);
            if (personOpt.isPresent()) {
                model.addAttribute(ViewNames.PERSON, personOpt.get());
                return ViewNames.PERSON_UPDATE;
            } else {
                log.warn("Person with ID: {} not found for update", personId);
                return ViewNames.ERROR;
            }
        } catch (Exception e) {
            log.error("Error occurred while preparing update form for person with ID: {}", personId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/update/{personId}")
    public String updatePersonById(@PathVariable("personId") int personId,
                                   @Valid @ModelAttribute(ViewNames.PERSON) PersonDTO personDTO,
                                   BindingResult result) {
        log.info("Attempting to update person with ID: {}", personId);

        if (result.hasErrors()) {
            log.warn("Validation failed during update for person ID {}: {}", personId, result.getAllErrors());
            return ViewNames.PERSON_UPDATE;
        }

        try {
            personService.updatePersonById(personId, personDTO);
            log.info("Person with ID: {} updated successfully", personId);
            return "redirect:/person/" + personId;
        } catch (Exception e) {
            log.error("Error occurred while updating person with ID: {}", personId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/delete/{personId}")
    public String deletePersonById(@PathVariable("personId") int personId) {
        log.info("Attempting to delete person with ID: {}", personId);

        if (personId <= 0) {
            log.warn("Invalid person ID: {}. Must be positive.", personId);
            return ViewNames.SHOW_ONE_PERSON;
        }

        try {
            Optional<PersonDTO> personOpt = personService.getPersonById(personId);
            if (personOpt.isEmpty()) {
                log.warn("Person with ID: {} not found for deletion", personId);
                return ViewNames.SHOW_ONE_PERSON;
            }

            personService.deletePersonById(personId);
            log.info("Person with ID: {} deleted successfully", personId);
            return ViewNames.REDIRECT_PERSON;
        } catch (Exception e) {
            log.error("Error occurred while deleting person with ID: {}", personId, e);
            return ViewNames.ERROR;
        }
    }
}