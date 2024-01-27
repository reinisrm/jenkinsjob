package com.example.InventorySystem.services.impl;

import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.repos.PersonRepo;
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

    private final Logger logger = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Autowired
    private PersonRepo personRepo;

    @Override
    public List<Person> getAll() {
        try {
            return (List<Person>) personRepo.findAll();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all persons", e);
            throw e;
        }
    }

    @Override
    public Optional<Person> getPersonById(int personId) {
        try {
            return personRepo.findById(personId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching person with ID: {}", personId, e);
            throw e;
        }
    }

    @Override
    public void createPerson(Person person) {
        try {
            if (person != null && person.getPersonId() == 0) {
                personRepo.save(person);
            } else {
                throw new IllegalArgumentException("Invalid person data or person_id already exists");
            }
        } catch (Exception e) {
            logger.error("Error occurred while creating person", e);
            throw e;
        }
    }

    @Override
    public void updatePersonById(int personId, Person updatedPersonData) {
        try {
            Optional<Person> existingPersonOptional = personRepo.findById(personId);
            if (existingPersonOptional.isPresent()) {
                Person existingPerson = existingPersonOptional.get();
                if (updatedPersonData != null && existingPerson.getPersonId() == personId) {
                    existingPerson.setName(updatedPersonData.getName());
                    existingPerson.setSurname(updatedPersonData.getSurname());
                    existingPerson.setPhoneNumber(updatedPersonData.getPhoneNumber());
                    existingPerson.setCourseName(updatedPersonData.getCourseName());
                    personRepo.save(existingPerson);
                } else {
                    throw new IllegalArgumentException("Invalid person_id or data");
                }
            } else {
                throw new NoSuchElementException("Person not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating person with ID: {}", personId, e);
            throw e;
        }
    }

    @Override
    public void deletePersonById(int personId) {
        try {
            Optional<Person> existingPersonOptional = personRepo.findById(personId);
            if (existingPersonOptional.isPresent()) {
                personRepo.deleteById(personId);
            } else {
                throw new NoSuchElementException("Person not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting person with ID: {}", personId, e);
            throw e;
        }
    }
}
