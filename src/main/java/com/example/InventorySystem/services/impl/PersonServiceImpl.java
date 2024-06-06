package com.example.InventorySystem.services.impl;

import com.example.InventorySystem.models.Lending;
import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.models.User;
import com.example.InventorySystem.repos.PersonRepo;
import com.example.InventorySystem.repos.UserRepo;
import com.example.InventorySystem.services.LendingService;
import com.example.InventorySystem.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    private PersonRepo personRepo;

    @Autowired
    private LendingService lendingService;

    @Autowired
    private UserRepo userRepo;

    @Override
    public List<Person> getAll() {
        List<Person> personList = personRepo.findAll();
        return personList;
    }

    @Override
    public Optional<Person> getPersonById(int personId) {
        Optional<Person> personOptional = personRepo.findById(personId);
        if (personOptional.isEmpty()) {
            log.warn("Person with id: {} is not found", personId);
        } else {
            log.info("Person found with id: {}", personId);
        }
        return personOptional;
    }

    @Override
    public void createPerson(Person person, int userId) {
        try {
            if (person != null && person.getPersonId() == 0) {
                Optional<User> userOptional = userRepo.findById(userId);
                userOptional.ifPresent(person::setUser); // associating the User with the Person
                personRepo.save(person);
                log.info("Person created successfully with associated user: {}", person);
            } else {
                throw new IllegalArgumentException("Invalid person data or person_id already exists");
            }
        } catch (Exception e) {
            log.error("Error occurred while creating person", e);
            throw e;
        }
    }

    @Override
    public void updatePersonById(int personId, Person updatedPersonData) {
        try {
            Optional<Person> existingPersonOptional = personRepo.findById(personId);
            if (existingPersonOptional.isPresent()) {
                Person existingPerson = existingPersonOptional.get();

                existingPerson.setName(updatedPersonData.getName());
                existingPerson.setSurname(updatedPersonData.getSurname());
                existingPerson.setPhoneNumber(updatedPersonData.getPhoneNumber());
                existingPerson.setCourseName(updatedPersonData.getCourseName());

                Person updatedPerson = personRepo.save(existingPerson);

                if (updatedPerson.getBorrowing() != null) {
                    for (Lending lending : updatedPerson.getBorrowing()) {
                        lending.setBorrower(updatedPerson);
                        lendingService.updateLending(lending.getLendingId(), lending);
                    }
                }
                if (updatedPerson.getLending() != null) {
                    for (Lending lending : updatedPerson.getLending()) {
                        lending.setLender(updatedPerson);
                        lendingService.updateLending(lending.getLendingId(), lending);
                    }
                }

                log.info("Person with id: {} updated successfully: {}", personId, updatedPerson);
            } else {
                throw new NoSuchElementException("Person not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating person with ID: {}", personId, e);
            throw e;
        }
    }


    @Override
    public void deletePersonById(int personId) {
        try {
            Optional<Person> existingPersonOptional = personRepo.findById(personId);
            if (existingPersonOptional.isPresent()) {
                Person existingPerson = existingPersonOptional.get();
                // remove person as a borrower in lending
                for (Lending lending : existingPerson.getBorrowing()) {
                    lending.setBorrower(null);
                }
                // remove person as a lender in lending
                for (Lending lending : existingPerson.getLending()) {
                    lending.setLender(null);
                }

                personRepo.deleteById(personId);
                log.info("Person with id: {} deleted successfully", personId);
            } else {
                throw new NoSuchElementException("Person not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting person with id: {}", personId, e);
            throw e;
        }
    }
}
